select
SISUserUID,
Email,
SISID, 
'EXEC [SIS].[dbo].[SP_SyncAccountDetails] 
N''' + Email + ''',
N'''+ isnull(IDNumber,'') + ''' , 
N'''+ SISUserAliasEmail+ ''', 
N'''+ SISUserUID+ ''' , 
N'''+ SISID+ ''' ,
N'''+ isnull(SISUserNameEn,'') + ''' , 
N'''+ SISUserNameAr+ ''', 
N'''+ SISUserRole+''''
from
(
select ph.Value Email, p.IDNumber, 
'SST11Pers' + convert(varchar(max), p.person_Id) + '@ese.gov.ae' SISUserAliasEmail, 
'SST-1-1-Pers-' + convert(varchar(max), p.person_Id) SISUserUID,
p.FirstName + ' ' + p.FamilyName SISUserNameAr,
p.EnglishFirstName + ' ' + p.EnglishFamilyName SISUserNameEn,
'["ADMIN"]' SISUserRole,
p.ExternalID SISID
from COMM_Phone ph 
join COMM_Person p on p.PhoneList_Id = ph.PhoneList_Id and contactType in (10)
join SYS_User u on p.User_Id = u.User_Id
where Ph.Value in (
'amed-Y.Ahmed@ese.gov.ae'
)
) account

select * from COMM_Person p  
--join COMM_Person_V pv on pv.Person_Id = p.Person_Id
where p.Person_Id = 4539942

select * from COMM_Person p  
--join COMM_Person_V pv on pv.Person_Id = p.Person_Id
where p.IDNumber = '784198252074707'


select * from MISC_Notification where Subject like '%5208101%'
--------- GET ACCOUNT EMAILS FROM OFFIC EMAIL AND ACCOUNT ID
select * from COMM_Phone where Value in
(
'ahmed-Y.Ahmed@ese.gov.ae'
)
select * from PRJ_AccountManagerState WHERE AccountID = 'Kerrion.Clarke@ese.gov.ae'

select * from COMM_Person p where p.phonelist_id = 10259129

--------- GET PERSON INFORMATION FROM OFFICIAL EMAIL
select * from COMM_Person p
--join SYS_User u on p.User_Id = u.User_Id
join COMM_Phone ph on ph.PhoneList_Id = p.PhoneList_Id 
where 
ph.Value = 'lama.labban@ese.gov.ae'
--and p.ExternalID = 'STAFF.86490AD'
p.IDNumber = '784200806387062'


select * from COMM_Person where ExternalID = 'STAFF.86490AD'


select * from DIC_Param where Name LIKE '%FACTOR%'



select * from DIC_Dictionary where Name like '%Subject%'
select * from DIC_DictionaryEntry WHERE Dictionary_Id = 39



select * from PRJ_AccountManagerState
WHERE AccountID = ''

select * from COMM_Phone where Value = 'Yasmina.Keis@ese.gov.ae'
select * from PRJ_AccountManagerState WHERE AccountID = 'Intesar.Aljenaibi@ese.gov.ae'


--------- GET PERSON INFORMATION FROM OFFICIAL EMAIL
select * from COMM_Person p
join COMM_Phone ph on ph.PhoneList_Id = p.PhoneList_Id and ph.ContactType = 10
join PRJ_AccountManagerState ams on ams.person_id = p.person_id
where 
ph.value = 'Mona.Almahri@ese.gov.ae'

----------------------------------------------
	select * from ADM_Student s
	join COMM_Person p on s.Student_Id = p.Person_Id
	join COMM_Phone ph on ph.PhoneList_Id = p.PhoneList_Id
	left outer join PRJ_AccountManagerState ams on p.Person_Id = ams.Person_Id
	WHERE s.StudentNumber = '2017076734'

	
Stum793104@ese.gov.ae
----------------------------------------------
select * from ADM_Student s
join COMM_Person p on s.Student_Id = p.Person_Id
join COMM_Phone ph on ph.PhoneList_Id = p.PhoneList_Id
left outer join PRJ_AccountManagerState ams on p.Person_Id = ams.Person_Id
WHERE ph.Value in
(
'stum20210014724@ese.gov.ae',
'stuf20220020388@ese.gov.ae',
'stum20190017099@ese.gov.ae',
'stum20190015975@ese.gov.ae',
'stum20220009471@ese.gov.ae',
'stum20200000593@ese.gov.ae',
'stum20230022553@ese.gov.ae',
'stum20190003227@ese.gov.ae',
'stuf20180009761@ese.gov.ae',
'stuf20200050460@ese.gov.ae',
'stuf2016081500@ese.gov.ae',
'stuf2017057962@ese.gov.ae',
'stuf2014025708@ese.gov.ae',
'stuf20190005056@ese.gov.ae'
)


 select 
 p.englishFirstname + ' ' + p.englishsecondname + ' ' + englishthirdname + ' '+ p.englishfamilyname,
 p.person_id,
 p.externalId,
 p.idnumber,
 ph.value
 from ADM_staff s
 join COMM_Person p on s.Staff_id = p.person_id 
 join COMM_Phone ph on p.phonelist_id = ph.PhoneList_Id and ph.ContactType in (10, 4)
 join SYS_User u on u.user_id = p.user_id
 --join RIGHT_UserProfile up on u.user_id = up.user_id
 --join RIGHT_Profile pr on up.profile_id = pr.profile_id
 where s.staffNumber in
  ( '24040AD', '31637AD')

 24040AD, 31637AD

 select top 100 * from MISC_Notification where Subject like '%Reset%'

 select * from ADM_Enrollment e 
 join PRJ_AccountManagerState ams on e.student_id = ams.person_id
 where e.Student_Id in (
 select s.student_id from ADM_Student s where s.studentNumber = '20220019714')

 select * from MISC_Notification where Subject like '%13586023%'

11617984
12445135
12865717
14656688
14817711

select top 1 * from STRUCT_Site 
select top 1 * from ADM_School
SELECT top 1 * FROM ADM_Session
SELECT top 1 * FROM ADM_SessionDef

SELECT 
distinct
s.staff_id,
s.staffNumber,
ss.Code
FROM 
ADM_Staff s 
join ADM_StaffAssign sa on sa.staff_id = s.staff_id and sa.EndDate is null
join STRUCT_SIte ss on ss.site_id = sa.AssignmentSite_Id
join COMM_Person p on s.staff_id = p.person_id
join COMM_Phone ph on ph.phonelist_id = p.phonelist_id and contacttype = 10
left join SYS_User u on u.user_id = p.user_id
where 
u.User_Id is null
and s.EndDate is null
and s.isDeleted = 0
and s.isTerminated = 0
and ph.value like '%ese.gov.ae%' or ph.value like '%kbza.sch.ae%' 







select * from ADM_Student where studentNumber = '20230009463'
select * from COMM_Person where person_id = 1713259

select * from COMM_Person_V p
join SYS_User u on p.userStamp = u.user_id
where p.person_id in 
(
4569778
)

-------------------------------------------------------------


select
*
from ADM_Staff s 
--join ADM_StaffAssign sa on s.staff_id = sa.staff_id
--join STRUCT_Site ss on sa.assignmentsite_id = ss.site_id
--join ADM_School sc on ss.site_id = sc.school_id
join COMM_Person p on s.staff_id = p.person_id
join COMM_Phone ph on p.phonelist_id = ph.phonelist_id and contacttype in (10,4)
left join PRJ_Accountmanagerstate ams on ams.person_id = s.staff_id
join SYS_User u on u.User_Id = p.user_id
where 
--s.EndDate is null
--and sa.enddate is null
--and sc.EducationType = 1
--s.staff_id = 5243453
ph.value in
(
'Lama.Labban@ese.gov.ae'
)


SQL:zmedcommon.COMM_Person:U:5243453
SQL:zmedcommon.COMM_Person:U:7926473

select * from MISC_Notification 
where 
HandlerName = 'AccountManagerSynch' and NextProcessingTime > '20231107'
and (Subject like 'SQL:zmedcommon.COMM_Person:U:%')

select top 100 * from MISC_Notification 
where 
HandlerName = 'AccountManagerSynch' and NextProcessingTime > '20231105'
and (Subject like '%ADM_StaffAssign%')


select 
distinct
ss.site_id,
ss.Name,
ss.EnglishName,
ss.code,
anm.anonymizedName
from STRUCT_Site ss
join ADM_School sc on ss.site_id = sc.school_id and sc.OperationalStatus = 2
join ADM_SchoolLicense scl on sc.school_id = scl.school_id and scl.iscurrent = 1
join PRJ_AccountManagerAnymz anm on anm.site_id = sc.school_id
where 
sc.educationType in (1, 3)

select * from PRJ_AccountManagerAnymz


select * from ADM_Student s
join COMM_Person p on s.student_id = p.person_id
--join COMM_Person_V pv on p.Person_Id = pv.person_id
--join PRJ_AccountManagerState ams on ams.Person_Id = p.person_id
join SYS_User u on p.user_id = u.user_id
join COMM_Phone ph on ph.phonelist_id = p.phonelist_id and contacttype = 10
where ph.value in (
'Stum783710@ese.gov.ae',
'stum2018014987@ese.gov.ae',
'STUM2011037942@ese.gov.ae',
'S679796@ese.gov.ae',
'stuf637330@ese.gov.ae'
)



select 
distinct
'SST-1-1-Pers-' + convert(nvarchar(max),s.staff_id),
ams.AccountID,
ph.value,
anm.AnonymizedName,
sa.AssignmentSite_Id,
'SST-1-1-Site-'+ + convert(nvarchar(max),sa.AssignmentSite_Id)
from ADM_Staff s 
join ADM_StaffAssign sa on s.staff_id = sa.staff_id
--join STRUCT_Site ss on sa.assignmentsite_id = ss.site_id
--join ADM_School sc on ss.site_id = sc.school_id
join COMM_Person p on s.staff_id = p.person_id
join COMM_Phone ph on p.phonelist_id = ph.phonelist_id and contacttype in (10,4)
left join PRJ_Accountmanagerstate ams on ams.person_id = s.staff_id
left join PRJ_AccountManagerAnymz anm on anm.person_id = s.staff_id
where 
ph.value in
(
'Nadjib.Abbidi@actvet.ac.ae',
'Kholoud.Alzaiter@actvet.gov.ae',
'Hamza.Alfares@actvet.gov.ae',
'Jabeen.Ahmed@actvet.gov.ae'
)




select * from STRUCT_Site where Code = 'DEWA'