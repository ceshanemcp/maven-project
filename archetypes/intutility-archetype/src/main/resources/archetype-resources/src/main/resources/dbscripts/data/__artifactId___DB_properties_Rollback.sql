DELETE FROM INT_UTILITY.INTG_CONFIG WHERE process_id IN(select process_id 
from process WHERE PROCESS_CODE = '${artifactId}');

DELETE FROM INT_UTILITY.PROCESS WHERE PROCESS_CODE = '${artifactId}';

commit;