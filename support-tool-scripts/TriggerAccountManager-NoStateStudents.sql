IF OBJECT_ID ( 'triggerAccountManagerNotification', 'P' ) IS NOT NULL DROP PROCEDURE triggerAccountManagerNotification
GO
CREATE PROCEDURE triggerAccountManagerNotification (@triggered_enrollment_id INT)
AS
	declare @user_id INT
	Begin
		PRINT 'Start Triggerring Notification for Enrollment ' + CONVERT(VARCHAR(10), @triggered_enrollment_id) 

		set @user_id = (@triggered_enrollment_id % 10) + 1

		-- prepare notification triggering for current enrollment
		declare @notifId INT
		exec GetSeqVal 'MISC_Notification', @notifId output

		insert into MISC_Notification ( Notification_Id, SenderSession_Id, SenderUser_Id, EmissionTime, Subject, HandlerName, NumberOfRetries, NextProcessingTime)
		values(@notifId, 1, @user_id, dbo.ISODATETIME(GETDATE()), 'SQL:zlmsadmin.ADM_Enrollment:I:'+CONVERT(VARCHAR(MAX), @triggered_enrollment_id), 'AccountManagerSynch', 1, dbo.ISODATETIME(GETDATE()))

		PRINT 'Finished Triggerring Notification for Enrollment ' + CONVERT(VARCHAR(10), @triggered_enrollment_id) 
	END
GO

---------------------------------------------------------------------------------
begin transaction
begin try

	DECLARE @enrollment_id INT	
	DECLARE c1 CURSOR READ_ONLY
	FOR	---- query to select all students in public schools with no state
		(select distinct top 2				-- this line must be changed based on the batch size decided (EX. 10, 100, 10000)
		e.Enrollment_Id
		from ADM_Enrollment e
		join ADM_Session ses on ses.Session_Id = e.Session_Id
		join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id
		join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id
		join COMM_Person p on p.Person_Id = e.Student_Id
		left join COMM_Phone ph on ph.PhoneList_Id = p.PhoneList_Id and ph.ContactType = 10
		left join PRJ_AccountManagerState ac on ac.Person_Id = e.Student_Id
		where ses.AcademicYear_Id = 2024 
		and e.EnrollmentType = 2
		and e.ExitDate is null
		and epd.EducationType = 1
		and epd.EducationPath_Id in (801, 804) --MOE And Ajyal
		and e.IsDeleted = 0
		and ph.Phone_Id IS null				    -- students with no official email
		and ac.AccountManagerState_Id IS null	-- students with no state
		)

	OPEN c1

	FETCH NEXT FROM c1
	INTO @enrollment_id

	WHILE @@FETCH_STATUS = 0
		BEGIN
			--PRINT @enrollment_id
			 Exec triggerAccountManagerNotification @enrollment_id
			FETCH NEXT FROM c1
			INTO @enrollment_id
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

DROP PROCEDURE triggerAccountManagerNotification
exec maintainseq


------------------------------------------------------------------------------------------------------------------------------------------------

select MISC_Notification.SenderUser_Id, NumberOfRetries, count(*) from MISC_Notification where 
HandlerName = 'AccountManagerSynch' and NextProcessingTime > '20230910'
and Subject like 'SQL:zlmsadmin.ADM_Enrollment:I:%'
group by NumberOfRetries, MISC_Notification.SenderUser_Id
order by count(*) desc

------------------------------

declare @notifId INT
exec GetSeqVal 'MISC_Notification', @notifId output
insert into MISC_Notification ( Notification_Id, SenderSession_Id, SenderUser_Id, EmissionTime, Subject, HandlerName, NumberOfRetries, NextProcessingTime)


select (2*((ROW_NUMBER() OVER(ORDER BY e.enrollment_Id)))) + @notifId, 1, 
case when (e.enrollment_Id % 10) + 1 = 2 then 1 else (e.enrollment_Id % 10) + 1 end , 
dbo.ISODATETIME(GETDATE()), 
'SQL:zlmsadmin.ADM_Enrollment:I:'+CONVERT(VARCHAR(MAX), 
e.enrollment_Id), 'AccountManagerSynch', 1, dbo.ISODATETIME(GETDATE())
from ADM_Enrollment e
join ADM_Session ses on ses.Session_Id = e.Session_Id
join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id
join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id
join COMM_Person p on p.Person_Id = e.Student_Id
left join COMM_Phone ph on ph.PhoneList_Id = p.PhoneList_Id and ph.ContactType = 10
left join PRJ_AccountManagerState ac on ac.Person_Id = e.Student_Id
where ses.AcademicYear_Id = 2024 
and e.EnrollmentType = 2
and e.ExitDate is null
and epd.EducationType = 1
--AND epd.EducationPath_Id = 2501	--kbza
and epd.EducationPath_Id in (801, 804) --MOE And Ajyal
and e.IsDeleted = 0		   
and p.IDNumber IN( 
'784201954149213')
exec maintainseq


---------------------------------------------------------------------------------------------------------------------------------------------------------------

--select distinct top 10
--e.Enrollment_Id,
--p.EnglishFirstName + ' ' + p.EnglishFamilyName AS StudentName,
--ph.Value AS OfficialEmail,
--ac.AccountID AccountManagerEmail
--from ADM_Enrollment e
--join ADM_Session ses on ses.Session_Id = e.Session_Id
--join ADM_SessionDef sd on sd.SessionDef_Id = ses.SessionDef_Id
--join ADM_EducationPathDef epd on epd.EducationPathDef_Id = sd.EducationPathDef_Id
--join COMM_Person p on p.Person_Id = e.Student_Id
--left join PRJ_AccountManagerState ac on ac.Person_Id = e.Student_Id and ac.AccountManagerState_Id = null -- have empty state
--join COMM_Phone ph on ph.PhoneList_Id = p.PhoneList_Id
--    and ph.ContactType = 10
--where ses.AcademicYear_Id = 2024 
--and e.ExitDate is null
--and epd.EducationType = 1
--and epd.EducationPath_Id in (801, 804) --MOE And Ajyal
--and e.IsDeleted = 0


--select top 10 * from MISC_Notification ORDER BY EmissionTime desc