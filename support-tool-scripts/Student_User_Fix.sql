/**
https://secondstepsoftware.atlassian.net/browse/SC-85130
*/

IF OBJECT_ID ( 'student_create_user_profile', 'P' ) IS NOT NULL DROP PROCEDURE student_create_user_profile
GO
CREATE PROCEDURE student_create_user_profile (@username VARCHAR(255), @password VARCHAR(20), @PersonId INT)
AS
BEGIN
  DECLARE @duplicate INT, @User_ID INT, @Res_ID INT, @Profile_id INT, @UserProfile_id INT, @CurrentTime VARCHAR(8), @userprofileid INT, @profileid int;
  SELECT @CurrentTime = FORMAT(GETDATE(), 'yyyyMMdd');
  print ('srart sp' )  
  
  exec GetSeqVal 'SYS_USER', @User_ID output
  exec GetSeqVal 'STRUCT_RESOURCE', @Res_ID output
  exec GetSeqVal 'RIGHT_UserProfile', @UserProfile_id output
     
  select @duplicate = user_ID from SYS_User where username=@username
  IF @duplicate is null
  BEGIN
  	insert into SYS_User (User_Id, UserName, UserPassword, UserIsEnabled) values (@User_ID, @username, @password, 1)
  	insert into STRUCT_Resource (Resource_Id, Name, Type, Status, User_Id, UserStamp,TimeStamp) values (@Res_ID, @username, 1, 1, @User_ID, 1,@CurrentTime)
    update COMM_Person set User_Id =@User_ID where Person_Id = @PersonId and User_Id is null
	print ('created user and resource ' + @username ) 
	set @Profile_id = (select profile_id from RIGHT_Profile where Name = 'studentportal')
	insert into RIGHT_UserProfile (UserProfile_Id, User_Id, Profile_Id, CanDelegate, DelegationScope, DisplayScenario, Position, UserStamp, TimeStamp, Site_Id) 
	values (@UserProfile_id, @User_ID, @Profile_id, 0, 0, 1, 0, 1, '20231220', NULL) 
  END
END
GO

IF OBJECT_ID('resourceGroup_Assign','P') IS NOT NULL DROP PROCEDURE resourceGroup_Assign
GO
CREATE PROCEDURE resourceGroup_Assign (@username VARCHAR(255), @SiteCode NVARCHAR(50))
AS
BEGIN
  DECLARE @RscGroup_ID INT, @RscGroupItem_ID INT, @Res_ID INT, @User_ID INT, @duplicate INT, @Rank INT, @fullname VARCHAR(50),@CurrentTime VARCHAR(20);
  exec GetSeqVal 'STRUCT_RSCGROUPITEM', @RscGroupItem_ID output
  SELECT @CurrentTime = FORMAT(GETDATE(), 'yyyyMMdd');
  SET @RscGroup_ID= (select top 1 ResourceGroup_ID from struct_site where code = @SiteCode  and isDeleted = 0)
  SET @User_ID = (select user_ID from SYS_User where username=@username)
  SET @Res_ID=(select Resource_Id from STRUCT_Resource where User_ID = @User_ID and isDeleted = 0)
  /*IF @Res_ID is null
  BEGIN
	 --SET @Res_ID=(select isnull(max(Resource_Id),0)+1 from STRUCT_Resource)
	 exec GetSeqVal 'STRUCT_RESOURCE', @Res_ID output
	 INSERT INTO STRUCT_Resource( Resource_Id, Name,Type,Status,User_ID,UserStamp,TimeStamp) VALUES (@Res_ID,@username,1,1,@User_ID,1,@CurrentTime)
  END*/
  select @duplicate = RscGroupItem_Id from STRUCT_RscGroupItem where RscGroup_Id= @RscGroup_ID and  Resource_Id= @Res_ID
  SET @Rank = (select isnull(max(Rank),0)+1 from STRUCT_RscGroupItem where RscGroup_Id = @RscGroup_Id)
  IF @duplicate is null
  BEGIN
	 INSERT INTO STRUCT_RscGroupItem (RscGroupItem_Id, RscGroup_Id, Resource_Id, Rank) VALUES (@RscGroupItem_ID, @RscGroup_ID, @Res_ID, @Rank)
  END
END
GO


begin transaction
begin try

	DECLARE @person_id INT
	DECLARE @gender INT
	DECLARE @student_number nvarchar(max)	
	DECLARE @site_code varchar(max)	
	DECLARE @username nvarchar(max)	
	DECLARE @password nvarchar(max)	

	DECLARE c1 CURSOR READ_ONLY
	FOR
		(select 
		distinct
		p.Person_Id,
		P.gender,
		s.studentNumber,
		ss.Code
		from ADM_Enrollment e
		join STRUCT_Site ss on e.School_Id = ss.Site_Id
		join ADM_Student s on s.Student_Id = e.Student_Id
		join ADM_Session ses on ses.Session_Id = e.Session_Id
		join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id
		join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id
		join COMM_Person p on p.Person_Id = e.Student_Id
		left JOIN SYS_User u on p.User_Id = u.User_Id
		where 
		ses.AcademicYear_Id = 2024 
		and e.EnrollmentType = 2
		and e.ExitDate is null
		and epd.EducationType in (1, 3)
		and e.IsDeleted = 0	
		and p.IsDeleted = 0	
		and u.User_Id is null
		)

	OPEN c1

	FETCH NEXT FROM c1
	INTO @person_id, @gender, @student_number, @site_code 

	WHILE @@FETCH_STATUS = 0
		BEGIN

			set @username = 'stu' + case when @gender = 1 then 'm' else 'f' end + @student_number
			set @password = 'test123'

			PRINT @username + ' ' + @password + ' ' + @student_number + ' ' + @site_code

			exec student_create_user_profile @username, @password , @person_id
			exec resourceGroup_Assign @username , @site_code

			FETCH NEXT FROM c1
			INTO @person_id, @gender, @student_number, @site_code
		END

	CLOSE c1
	DEALLOCATE c1

END TRY
BEGIN CATCH
    ROLLBACK TRANSACTION;
	CLOSE c1
	DEALLOCATE c1
    SELECT ERROR_NUMBER() AS ErrorNumber, 
           ERROR_MESSAGE() AS ErrorMessage, 
           ERROR_LINE() AS ERRORLINE;
END CATCH;
IF @@TRANCOUNT > 0
    COMMIT TRANSACTION;
GO
