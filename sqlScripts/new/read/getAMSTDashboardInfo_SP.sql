IF OBJECT_ID ( 'getAMSTDashboardInfo', 'P' ) IS NOT NULL DROP PROCEDURE getAMSTDashboardInfo
GO
CREATE PROCEDURE getAMSTDashboardInfo (
	@academicYear int,
	@educationTypes nvarchar(max),
	@studentsWithNoUserRecord INT OUTPUT,
	@studentsWithDisabledUsers INT OUTPUT,
	@studentsWithNoProfileRecord INT OUTPUT,
	@studentsWithNoStateRecord INT OUTPUT)
AS
	Begin
		PRINT 'Start Triggering getAMSTDashboardInfo '

		set @studentsWithNoUserRecord = (
			select
			count(e.Enrollment_Id)
			from ADM_Enrollment e
			join STRUCT_Site ss on e.School_Id = ss.Site_Id
			join ADM_Student s on s.Student_Id = e.Student_Id
			join ADM_Session ses on ses.Session_Id = e.Session_Id
			join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id
			join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id
			join COMM_Person p on p.Person_Id = e.Student_Id
			left JOIN SYS_User u on p.User_Id = u.User_Id
			where
			ses.AcademicYear_Id = @academicYear
			and e.EnrollmentType = 2
			and e.ExitDate is null
			and epd.EducationType in (
				SELECT CAST(value AS INT)
				FROM STRING_SPLIT(@educationTypes, ','))
			and e.IsDeleted = 0
			and p.IsDeleted = 0
			and u.User_Id is null
		)

		set @studentsWithDisabledUsers = (
			select
			count(u.user_id)
			from ADM_Enrollment e
			join STRUCT_Site ss on e.School_Id = ss.Site_Id
			join ADM_Student s on s.Student_Id = e.Student_Id
			join ADM_Session ses on ses.Session_Id = e.Session_Id
			join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id
			join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id
			join COMM_Person p on p.Person_Id = e.Student_Id
			join SYS_User u on u.user_id = p.user_id
			where
			ses.AcademicYear_Id = @academicYear
			and e.EnrollmentType = 2
			and e.ExitDate is null
			and epd.EducationType in (
				SELECT CAST(value AS INT)
				FROM STRING_SPLIT(@educationTypes, ','))
			and e.IsDeleted = 0
			and p.IsDeleted = 0
			and u.UserIsEnabled = 0
		)

		set @studentsWithNoProfileRecord = (
			select
			count(u.user_id)
			from ADM_Enrollment e
			join STRUCT_Site ss on e.School_Id = ss.Site_Id
			join ADM_Student s on s.Student_Id = e.Student_Id
			join ADM_Session ses on ses.Session_Id = e.Session_Id
			join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id
			join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id
			join COMM_Person p on p.Person_Id = e.Student_Id
			join SYS_User u on u.user_id = p.user_id
			left JOIN RIGHT_UserProfile up on up.user_id = u.user_id and up.Profile_Id = 39
			where
			ses.AcademicYear_Id = @academicYear
			and e.EnrollmentType = 2
			and e.ExitDate is null
			and epd.EducationType in (
				SELECT CAST(value AS INT)
				FROM STRING_SPLIT(@educationTypes, ','))
			and e.IsDeleted = 0
			and p.IsDeleted = 0
			and (up.UserProfile_Id is null)
		)

		set @studentsWithNoStateRecord = (
			select
			count(e.Enrollment_Id)
			from ADM_Enrollment e
			join ADM_Session ses on ses.Session_Id = e.Session_Id
			join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id
			join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id
			join COMM_Person p on p.Person_Id = e.Student_Id
			left join PRJ_AccountManagerState ac on ac.Person_Id = e.Student_Id
			where ses.AcademicYear_Id = @academicYear
			and e.EnrollmentType = 2
			and e.ExitDate is null
			and epd.EducationType in (
				SELECT CAST(value AS INT)
				FROM STRING_SPLIT(@educationTypes, ','))
			and e.IsDeleted = 0
			and ac.AccountManagerState_Id IS null
		)
		PRINT 'Finish Triggering getAMSTDashboardInfo '
	END
GO

-------------- THIS PART TO TEST ONLY --------------
--declare @studentsWithNoUserRecord INT
--declare @studentsWithDisabledUsers INT
--declare @studentsWithNoProfileRecord INT
--declare @studentsWithNoStateRecord INT

--Exec getAMSTDashboardInfo
--	2024,
--	'1,3',
--	@studentsWithNoUserRecord OUTPUT,
--	@studentsWithDisabledUsers OUTPUT,
--	@studentsWithNoProfileRecord OUTPUT,
--	@studentsWithNoStateRecord OUTPUT

--print CONVERT(VARCHAR(10), @studentsWithNoUserRecord) + ' ' + CONVERT(VARCHAR(10), @studentsWithDisabledUsers)  + ' ' + CONVERT(VARCHAR(10), @studentsWithNoProfileRecord)   + ' ' + CONVERT(VARCHAR(10), @studentsWithNoStateRecord)



