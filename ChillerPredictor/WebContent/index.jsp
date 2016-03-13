<!DOCTYPE html>
<meta charset="utf-8">

<!-- <link rel="styleshee" type="text/css" href="css/jquery.timepicker.css" />
<link rel="stylesheet" type="text/css" href="css/bootstrap-datepicker.css" /> -->
<link rel="stylesheet" href="css/style.css">
<body>
 
<!-- <script src="http://code.jquery.com/jquery-1.10.2.js" type="text/javascript"></script> -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<script src="//d3js.org/d3.v3.min.js"></script>
<script src="js/index.js"> </script>


<script type="text/javascript" src="js/datepair.js"></script>
<script type="text/javascript" src="js/jquery.timepicker.js"></script>
<script type="text/javascript" src="js/bootstrap-datepicker.js"></script> 
<h1> Chiller Energy Prediction Dashboard </h1> 
<hr>
<div> 
<div id="graph" class = "graph"> </div>
 
<div id ="control" class = "control">  
<p id="eventsExample">
    <input type="text" class="date start" /> 
    <input type="text" class="time start" />  <br> to <br> 
    <input type="text" class="time end" />
    <input type="text" class="date end" />
</p>

<p id = "eventsExampleStatus"></p> 
</div> 
</div>

<script>


 // initialize input widgets first
    $('#eventsExample .time').timepicker({
        'showDuration': true,
        'timeFormat': 'g:ia'
    });

    $('#eventsExample .date').datepicker({
        'format': 'm/d/yyyy',
        'autoclose': true
    });

    var eventsExampleEl = document.getElementById('eventsExample');
    var eventsExampleDatepair = new Datepair(eventsExampleEl);

    // some sample handlers
    $('#eventsExample').on('rangeSelected', function(){
        $('#eventsExampleStatus').text('Valid range selected');
    }).on('rangeIncomplete', function(){
        $('#eventsExampleStatus').text('Incomplete range');
    }).on('rangeError', function(){
        $('#eventsExampleStatus').text('Invalid range');
    });
    
</script>
 
 

</body>
 



 

 
 