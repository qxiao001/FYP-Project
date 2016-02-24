select DISTINCT ON (p.time_stamp) 'chiller1' as chiller_name,p.time_stamp as powerts1,c.time_stamp as conts1,e.time_stamp as evats1,
c.flow_rate as conflow1,c.inlet_temp as conin1,c.outlet_temp as conout1,
e.flow_rate as evaflow1,e.inlet_temp as evain1,e.outlet_temp as evaout1,
total_positive_watt_hour as consumption, (p.ch1_watt+p.ch2_watt+p.ch3_watt) as power1  
from "Ultrasonic_flow_modbus_log_#yyyy_mm#" c,"Ultrasonic_flow_modbus_log_#yyyy_mm#" e, "Meter_log_#yyyy_mm#" p 
where c.my_id = 11006 and p.my_id = 65431 and e.my_id = 59358 
and (e.time_stamp-p.time_stamp)< INTERVAL '30 seconds'
and (p.time_stamp-e.time_stamp)< INTERVAL '30 seconds'  
and (c.time_stamp-p.time_stamp)< INTERVAL '30 seconds' 
and (p.time_stamp-c.time_stamp) < INTERVAL '30 seconds'
and c.ultrasonic_flow_modbus_log_seq > #ultra_min# and c.ultrasonic_flow_modbus_log_seq <= #ultra_max# 
and e.ultrasonic_flow_modbus_log_seq > #ultra_min# and e.ultrasonic_flow_modbus_log_seq <= #ultra_max# 
and p.meter_log_seq > #power_min# and p.meter_log_seq <= #power_max#;