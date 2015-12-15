insert into "chiller_cbpt2" 
select distinct on (c.timestamp)
date_trunc('hour',c.timestamp)as hourstamp,c.*, t.temp, r.now
from "chiller_cbpt" c, "taiwan_temp" t, "taiwan_rain" r 
where (c.timestamp-t.timestamp)< INTERVAL '600 seconds'
and (t.timestamp-c.timestamp)< INTERVAL '600 seconds'  
and (c.timestamp-r.timestamp)< INTERVAL '600 seconds' 
and (r.timestamp-c.timestamp) < INTERVAL '600 seconds'
and t.stationid='C0F850' and r.stationid='C0F850'
and (c.timestamp - '#chillercombts#'::timestamp) > '30 seconds';

update "stamps" set
chillercombts = (select timestamp from "chiller_cbpt" order by timestamp desc limit 1)