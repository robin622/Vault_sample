<table id="statics" style="min-width: 400px; height: 200px; margin: 0 auto">
 <thead>
    <tr>
      <th class="no-underline"><a href="#" class="float-right no-underline"></a></th>      
      </tr>
    </thead>
<thead>

<tr>   
<td>
<span class="green-font ">New requests and Active users Trend by Day/Week/Month</span>
<div class="line5"></div>
				<div id="tab">
					<ul class="nav nav-tabs">
					    <li class="active"><a id="1" href="#day" data-toggle="tab">Daily</a></li>
						<li><a id="2" href="#week" data-toggle="tab">Weekly</a></li>
						<li><a id="3" href="#month" data-toggle="tab">Monthly</a></li>
					</ul>
					<div class="tab-pane fade container5" id="day"></div>
					<div class="tab-pane fade container5" id="week" style="display:none;" ></div>
					<div class="tab-pane fade container5" id="month" style="display:none;"></div>
				</div>				


<script type="text/javascript">
jQuery(function($){
	var url = "<%=request.getContextPath()%>/StaticsServlet";
	
	$.ajax({
		type: "POST",
		url: url,
		data: "",
		dataType:'json',
		success: function(rtnData) {

			$(".nav.nav-tabs").children().each(function() {
				$("#1").click(function(){
					 $("#day").show();
					 $("#week").hide();
					 $("#month").hide();
					 displayTicketTrendChart("day");
					 });
				 
                $("#2").click(function(){
                	 $("#day").hide();
					 $("#week").show();
					 $("#month").hide();
					 displayTicketTrendChart("week");
					 });

                $("#3").click(function(){
                	 $("#day").hide();
					 $("#week").hide();
					 $("#month").show();
					 displayTicketTrendChart("month");
					 });
				 
			});

			displayTicketTrendChart("day");
			

			function displayTicketTrendChart(container)
			{

				console.log("container="+container);
				if(container=="day")
				{
					var dateType=rtnData.day.dateType;
		            var resultRequest=rtnData.day.resultRequest;
		            var resultUserNumber=rtnData.day.resultUserNumber;
		            var resultUser=rtnData.day.resultUser;
					}
				else if(container=="week")
				{
					var dateType=rtnData.week.dateType;
		            var resultRequest=rtnData.week.resultRequest;
		            var resultUserNumber=rtnData.week.resultUserNumber;
		            var resultUser=rtnData.week.resultUser;
					}
				else if(container=="month")
				{
					var dateType=rtnData.month.dateType;
		            var resultRequest=rtnData.month.resultRequest;
		            var resultUserNumber=rtnData.month.resultUserNumber;
		            var resultUser=rtnData.month.resultUser;
					}
	            
			    var chart = new Highcharts.Chart({
	
	            chart: {
	                type: 'column',
	                renderTo:container
	            },
	            title: {
	                text: ''
	            },
	            subtitle: {
	            },
	            xAxis: {
	                categories: dateType,	               
	            },
	            yAxis: {
	                min: 0,
	                title: {
	                    text: 'number'
	                }
	            },
	             tooltip: {
	            	 style: {
		                    color: '#333333',
		                	fontSize: '12px',
			                padding: 10,
			                width: 200,
		                },
	            	
	                formatter: function() {
	                	if(this.series.name=="active users"){
	                	var i=0;
	                	var s='<b>'+ this.key +'</b><br/><b>'+
                        this.series.name +': </b>'+ this.point.y +'<br/>';
		                while(i<dateType.length)
			                {
                               if(this.x==dateType[i])
                               {                           	 
         	                        return s+"<b>Users:</b>"+resultUser[i];
         	                        break;
                                   }
                                   
                                   else
                                   {
                                	   i++;                    
                                       }
                                      
			                }
	                	}
	                	else
		                    return '<b>'+ this.key +'</b><br/><b>'+
	                        this.series.name +': </b>'+ this.point.y +'<br/>';
	                
	                }
	            },
	            plotOptions: {
	                column: {
	                    pointPadding: 0.2,
	                    borderWidth: 0
	                }
	            },
	           
	            series: [{
	                name: 'new created requests',
	                data: resultRequest,
	    
	            }, {
	                name: 'active users',
	                data: resultUserNumber,
	    
	            }]
	            
	        });

		        return chart;
			}

			
			}
			
	});
});
	

		</script>

		</td>
</tr>
</thead>

</table>