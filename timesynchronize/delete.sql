-- 删除2014年以前的指标数据--
delete from system_available_rate where report_date<'2014-01-01';
delete from system_transaction_success_rate where report_date<'2014-01-01';
delete from info_technology_risk_event_count where report_date<'2014-01-01';
delete from fake_site_attachment_rate where report_date<'2014-01-01';
delete from electronic_active_user_change_rate where report_date<'2014-01-01';
delete from electronic_transaction_change_rate where report_date<'2014-01-01';
delete from operation_change_success_rate where report_date<'2014-01-01';
delete from outside_attack_change_rate where report_date<'2014-01-01';