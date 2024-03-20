IF OBJECT_ID ( 'User_create', 'P' ) IS NOT NULL DROP PROCEDURE User_create
GO
CREATE PROCEDURE User_create (@username VARCHAR(255), @password VARCHAR(20), @PersonId INT)
AS
BEGIN
  DECLARE @duplicate INT, @User_ID INT, @Res_ID INT, @CurrentTime VARCHAR(8), @userprofileid INT, @profileid int;
  SELECT @CurrentTime = FORMAT(GETDATE(), 'yyyyMMdd');

  
  exec GetSeqVal 'SYS_USER', @User_ID output
  exec GetSeqVal 'STRUCT_RESOURCE', @Res_ID output
     
  select @duplicate = user_ID from SYS_User where username=@username
  IF @duplicate is null
  BEGIN
  	insert into SYS_User (User_Id, UserName, UserPassword, UserIsEnabled) values (@User_ID, @username, @password, 1)
  	insert into STRUCT_Resource (Resource_Id, Name, Type, Status, User_Id, UserStamp,TimeStamp) values (@Res_ID, @username, 1, 1, @User_ID, 1,@CurrentTime)
    update COMM_Person set User_Id =@User_ID where Person_Id = @PersonId and User_Id is null
	print ('created user and resource ' + @username )    
	--exec MaintainSeq
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
  SET @RscGroup_ID= (select ResourceGroup_ID from struct_site where code = @SiteCode  and isDeleted = 0)
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
	DECLARE @staffNumber varchar(max)		
	DECLARE @sitecode varchar(max)	
	DECLARE @username varchar(max)	
	DECLARE @password varchar(max)	
	DECLARE c1 CURSOR READ_ONLY
	FOR	
		(
		select distinct
		p.Person_Id,
		s.staffNumber,
		ss.Code
		from ADM_staff s
		join ADM_StaffAssign sa on sa.Staff_Id = S.Staff_Id
		join STRUCT_Site ss on ss.Site_Id = sa.AssignmentSite_Id
		JOIN COMM_Person p on p.Person_Id = s.Staff_Id
		left join SYS_User u on p.User_Id = u.User_Id
		join COMM_Phone ph on ph.PhoneList_Id = p.PhoneList_Id and ph.contactType in (10)
		where 
		u.User_Id is null
		and ph.Value in 
		(
		'Abdelmenem.Elgendy@ese.gov.ae'
		--'Mohamed-S.Mohamed@ese.gov.ae'
		)
	)

	OPEN c1

	FETCH NEXT FROM c1
	INTO @person_id, @staffNumber, @sitecode

	WHILE @@FETCH_STATUS = 0
		BEGIN
			set @username = 'u.'+@staffNumber
			set @password = 'test123'
			--print @username + ' ' + @password + ' ' + convert(nvarchar(max), @person_id) + ' ' + @sitecode
			EXEC User_create @username, @password, @person_id
			EXEC resourceGroup_Assign @username , @sitecode
			FETCH NEXT FROM c1
			INTO @person_id, @staffNumber, @sitecode
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

