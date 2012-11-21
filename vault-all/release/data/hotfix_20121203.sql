set SQL_SAFE_UPDATES = 0; 
update vault.VA_request_history set useremail=CONCAT(editedby,'@redhat.com') where historyid in (select * from (select historyid from vault.VA_request_history as his where useremail='null@redhat.com') as h)
set SQL_SAFE_UPDATES = 1;