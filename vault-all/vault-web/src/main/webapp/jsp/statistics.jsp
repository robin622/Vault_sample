<table id="statistics" style="min-width: 400px; height: 200px; margin: 0 auto; display: none;">
 <thead>
    <tr>
      <th class="no-underline"><a href="#" class="float-right no-underline"></a></th>      
      </tr>
    </thead>
<thead>

<tr>   
<td>
<span class="green-font">New requests and Active users Trend by Day/Week/Month</span>
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
<span class="purple-font">Group Utilization(
<span id="currentTime"></span>
)
</span>
<script type="text/javascript">
jQuery(function($){
	var currentDate = new Date();
	var str;
	if(currentDate.getMonth()+1==1)
	{
		  str="Jan,"+currentDate.getFullYear();
		  $("#currentTime").html(str);
	}else if(currentDate.getMonth()+1==2)
		{
		  str="Feb,"+currentDate.getFullYear();
		  $("#currentTime").html(str);
		}
	else if(currentDate.getMonth()+1==3)
	{
	  str="Mar,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	else if(currentDate.getMonth()+1==4)
	{
	  str="Apr,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	else if(currentDate.getMonth()+1==5)
	{
	  str="May,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	else if(currentDate.getMonth()+1==6)
	{
	  str="June,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	else if(currentDate.getMonth()+1==7)
	{
	  str="July,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	else if(currentDate.getMonth()+1==8)
	{
	  str="Aug,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	else if(currentDate.getMonth()+1==9)
	{
	  str="Sep,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	else if(currentDate.getMonth()+1==10)
	{
	  str="Oct,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	else if(currentDate.getMonth()+1==11)
	{
	  str="Nov,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	else if(currentDate.getMonth()+1==12)
	{
	  str="Dem,"+currentDate.getFullYear();
	  $("#currentTime").html(str);
	}
	
	
});
</script>
<div class="line2"></div>
<div class="tab-pane fade container7" id="group"></div>
		</td>
</tr>
</thead>
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

			var groups=rtnData.groups;
			var userGroupThisMonth=rtnData.userGroupThisMonth;
			var userGroupLastMonth=rtnData.userGroupLastMonth;
			console.log(groups);
			console.log(userGroupThisMonth);
			console.log(userGroupLastMonth);
			displayTicketTrendChart("day");

			function displayTicketTrendChart(container)
			{

				//console.log("container="+container);
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
	            /*	 style: {
		                	fontSize: '12px',
			                padding: 10,
			                width: 300,
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
	                
	                }*/
	            	 headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	                 pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                     '<td style="padding:0"><b>{point.y}</b></td></tr>',
	                 footerFormat: '</table>',
	                 shared: true,
	                 useHTML: true
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
	                color : '#7AACE2',
	                borderColor : '#2F7FD7'
	    
	            }, {
	                name: 'active users',
	                data: resultUserNumber,
	                color : '#C1DD86',
					borderColor : '#8BBC23'
	    
	            }]
	            
	        });

		        return chart;
			}

			 $('#group').highcharts({
		            chart: {
		                type: 'bar'
		            },
		            title: {
		                text: ''
		            },
		            subtitle: {
		                text: ''
		            },
		            xAxis: {
		                categories: groups,
		                title: {
		                    text: null
		                },

	                    width:50

	                
	                    
		            },
		            yAxis: {
		                min: 0,
		                title: {
		                    text: '',
		                    align: 'high'
		                },
		                labels: {
		                    overflow: 'justify'
		                }
		            },
		            tooltip: {
		            	useHTML : true,
						shared : true,
						headerFormat : '<small>{point.key}</small><table>',
						pointFormat : '<tr><td style="color: {series.options.borderColor}">{series.name}: </td>'
								+ '<td style="text-align: right"><b>{point.y}</b></td></tr>',
						footerFormat : '</table>'
		            },
		            plotOptions: {
		                bar: {
		                    dataLabels: {
		                        enabled: true
		                    }
		                }
		            },
		            legend: {
			            
		                backgroundColor: '#FFFFFF',
		                shadow: true,
		                reversed: true
		            },
		            credits: {
		                enabled: false
		            },
		            series: [{
		                name: 'This Month',
		                data: userGroupThisMonth,
		                color : '#B993B9',
						borderColor : '#773577'
		            }, {
		                name: 'Last Month',
		                data: userGroupLastMonth,
		                color : '#EFEFEF',
						borderColor : '#CCCCCC'
		            }]
		        });
			
			}	   
			
	});

	
});
	

		</script>

</table>