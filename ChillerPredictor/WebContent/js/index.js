var resultJsons = [ {
	"date" : "2015-10-14 22:27:26.974",
	"actual" : 1,
	"predicted" : 4
}, {
	"date" : "2015-10-15 22:27:26.974",
	"actual" : 2,
	"predicted" : 3
}, {
	"date" : "2015-10-16 22:27:26.974",
	"actual" : 3,
	"predicted" : 1
}, {
	"date" : "2015-10-17 22:27:26.974",
	"actual" : 4,
	"predicted" : 1
}
];

$(document).ready(function() {

	/*
	 * $.ajax({ url : "/ChillerPredictor/PowerServelet", dataType : 'json', type :
	 * "POST", success : function(data) { // window.alert("456" + data);
	 * resultJsons = data; window.alert("456" + resultJsons[1].actual);
	 * drawTrend(data); }, });
	 */
	// drawTrend(resultJsons);
	drawTrend(resultJsons);

});

function drawTrend3(resultJsons) {
	
	var formatDate = d3.time.format("%Y-%m-%d %H:%M:%S.%L").parse;
	
	resultJsons = JSON.stringify(resultJsons);
	
	data = JSON.parse(resultJsons);

	console.log(data[0]);

	data.forEach(function(d) {
		d.date = formatDate(d.date);
		d.actual = +d.actual;
		d.predicted =+ d.predicted;
	
	});
	
	console.log(data[0]);
	
	// Set the dimensions of the canvas / graph
	var	margin = {top: 30, right: 20, bottom: 30, left: 50},
		width = 600 - margin.left - margin.right,
		height = 270 - margin.top - margin.bottom;
	 
	// Parse the date / time
	
	 
	// Set the ranges
	var	x = d3.time.scale().range([0, width]);
	var	y = d3.scale.linear().range([height, 0]);
	 
	// Define the axes
	var	xAxis = d3.svg.axis().scale(x)
		.orient("bottom").ticks(5);
	 
	var	yAxis = d3.svg.axis().scale(y)
		.orient("left").ticks(5);
	 
	// Define the line for actual 
	var	valueline = d3.svg.line()
		.x(function(d) { return x(d.date); })
		.y(function(d) { return y(d.actual); });
	
	// Define the line for predicted 
	var	valueline2 = d3.svg.line()
		.x(function(d) { return x(d.date); })
		.y(function(d) { return y(d.predicted); });
	    
	// Adds the svg canvas
	var	svg = d3.select("body")
		.append("svg")
			.attr("width", width + margin.left + margin.right)
			.attr("height", height + margin.top + margin.bottom)
		.append("g")
			.attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	

	// Scale the range of the data
	x.domain(d3.extent(data, function(d) { return d.date; }));
	y.domain([0, d3.max(data, function(d) { return Math.max(d.actual, d.predicted); })]);
 
	// Add the valueline path.
	svg.append("path")	
		.attr("class", "line")
		.style("stroke", "blue")
		.attr("d", valueline(data))
		.attr("data-legend","Actual");
	
	svg.append("path")	
	.attr("class", "line")
	.style("stroke", "red")
	.attr("d", valueline2(data))
	.attr("data-legend","Predicted");
 
	// Add the X Axis
	svg.append("g")		
		.attr("class", "x axis")
		.attr("transform", "translate(0," + height + ")")
		.call(xAxis);
 
	// Add the Y Axis
	svg.append("g")		
		.attr("class", "y axis")
		.call(yAxis)
		.append("text")
	      .attr("transform", "rotate(-90)")
	      .attr("y", 6)
	      .attr("dy", ".71em")
	      .style("text-anchor", "end")
	      .text("Power (watt)");
	
	// Add the legend
	legend = svg.append("g")
    .attr("class","legend")
    .attr("transform","translate(50,30)")
    .style("font-size","12px")
    .call(d3.legend)

  setTimeout(function() { 
    legend
      .style("font-size","20px")
      .attr("data-style-padding",10)
      .call(d3.legend)
  },1000)
	
};




function drawTrend(resultJsons) {
	
	


	var margin = {
		top : 20,
		right : 80,
		bottom : 30,
		left : 50
	}, width = 960 - margin.left - margin.right, height = 500 - margin.top
			- margin.bottom;

	var parseDate = d3.time.format("%Y-%m-%d %H:%M:%S.%L").parse;

	var x = d3.time.scale().range([ 0, width ]);

	var y = d3.scale.linear().range([ height, 0 ]);

	var color = d3.scale.category10();

	var xAxis = d3.svg.axis().scale(x).orient("bottom");

	var yAxis = d3.svg.axis().scale(y).orient("left");

	var line = d3.svg.line().interpolate("basis").x(function(d) {
		return x(d.date);
	}).y(function(d) {
		return y(d.temperature);
	});

	

	var svg = d3.select("#graph").append("svg").attr("width",
			width + margin.left + margin.right).attr("height",
			height + margin.top + margin.bottom).append("g").attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");
	
	resultJsons = JSON.stringify(resultJsons);
	console.log(resultJsons);

	data = JSON.parse(resultJsons);

	console.log(data[0]);

	color.domain(d3.keys(data[0]).filter(function(key) {
		return key !== "date";
	}));

	data.forEach(function(d) {
		d.date = parseDate(d.date);
		d.actual = +d.actual;
		d.predicted = +d.predicted;
		
	});

	var cities = color.domain().map(function(name) {
		return {
			name : name,
			values : data.map(function(d) {
				return {
					date : d.date,
					temperature : +d[name]
				};
			})
		};
	});

	x.domain(d3.extent(data, function(d) {
		return d.date;
	}));

	y.domain([ d3.min(cities, function(c) {
		return d3.min(c.values, function(v) {
			return v.temperature;
		});
	}), d3.max(cities, function(c) {
		return d3.max(c.values, function(v) {
			return v.temperature;
		});
	}) ]);

	svg.append("g").attr("class", "x axis").attr("transform",
			"translate(0," + height + ")").call(xAxis);

	svg.append("g").attr("class", "y axis").call(yAxis).append("text").attr(
			"transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style(
			"text-anchor", "end").text("Power (watt)");

	var city = svg.selectAll(".city").data(cities).enter().append("g").attr(
			"class", "city");

	city.append("path").attr("class", "line").attr("d", function(d) {
		return line(d.values);
	}).style("stroke", function(d) {
		return color(d.name);
	});

	city.append("text").datum(function(d) {
		return {
			name : d.name,
			value : d.values[d.values.length - 1]
		};
	}).attr(
			"transform",
			function(d) {
				return "translate(" + x(d.value.date) + ","
						+ y(d.value.temperature) + ")";
			}).attr("x", 3).attr("dy", ".35em").text(function(d) {
		return d.name;
	});
	
	svg.append("text")
    .attr("x", (width / 2))             
    .attr("y", 0 - (margin.top / 2))
    .attr("text-anchor", "middle")  
    .style("font-size", "16px") 
    .style("text-decoration", "underline")  
    .text("Actual vs Predicted Power Graph");

}

