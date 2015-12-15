insert into "chiller_cbpt" 
select distinct on (a.powerts1)
a.powerts1 as timestamp, 
a.power1 as power1, b.power2 as power2, (a.power1+b.power2) as powertotal, 
a.consumption as consumption1, b.consumption as consumption2, (a.consumption+b.consumption) as consumptiontotal,
case when(a.conflow1 < 0) then 0 else a.conflow1 end as conflow1, case when (b.conflow2 < 0) then 0 else b.conflow2 end as conflow2, 
a.conin1 as conin1, b.conin2 as conin2, a.conout1 as conout1, b.conout2 as conout2,
case when (a.evaflow1 < 0) then 0 else a.evaflow1 end as evaflow1, case when (b.evaflow2 < 0) then 0 else b.evaflow2 end as evaflow2,
a.evain1 as evain1, b.evain2 as evain2, a.evaout1 as evaout1, b.evaout2 as evaout2
from "chiller1pt" a,"chiller2pt" b
where (a.powerts1 - b.powerts2) < INTERVAL '30 seconds'
and (b.powerts2 - a.powerts1) < INTERVAL '30 seconds'
and (a.powerts1- '#chiller1ts#'::timestamp) > '30 seconds' 
and (b.powerts2-'#chiller2ts#'::timestamp) > '30 seconds';

update "stamps" set
chiller1ts = (select powerts1 from "chiller1pt" order by powerts1 desc limit 1),
chiller2ts = (select powerts2 from "chiller2pt" order by powerts2 desc limit 1)