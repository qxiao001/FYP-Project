update "stamps" set 
ultra_max = (select ultrasonic_flow_modbus_log_seq from "Ultrasonic_flow_modbus_log_2015_11" order by ultrasonic_flow_modbus_log_seq desc limit 1),
power_max = (select meter_log_seq from "Meter_log_2015_11" order by meter_log_seq desc limit 1)
--ultra_max = ultra_min+20000,
--power_max = power_min+20000