DELETE FROM intg_config pc WHERE pc.process_id in (SELECT process_id FROM process WHERE process_name = '${artifactId}');
delete from process where process_name = '${artifactId}';
COMMIT ;
INSERT INTO process (process_name, process_code) VALUES ('${artifactId}', '${artifactId}');
COMMIT ;

INSERT INTO intg_config (process_id, env_id, config_type, env_depn_ind, config_key, config_value) 
                 VALUES ((SELECT process_id FROM process WHERE process_name = '${artifactId}'),
                         (select ENV_ID from ENVIRONMENT where env_name = 'DEV1'),
                         'Integration Properties', 
                         'N' ,
                         'te.nordic.message.lastSourceSyncSP',
                         'INT_UTILITY.AUDIT_UTILITY_PKG.GET_LAST_SOURCE_SYNC_TIME');
