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
					<ul class="nav nav-tabs"">
					    <li class="active"><a id="1" href="#dayly" data-toggle="tab">Daily</a></li>
						<li><a id="2" href="#weekly" data-toggle="tab">Weekly</a></li>
						<li><a id="3" href="#monthly" data-toggle="tab">Monthly</a></li>
					</ul>
					<div class="tab-pane fade container5" id="dayly"></div>
					<div class="tab-pane fade container5" id="weekly" style="display:none;" ></div>
					<div class="tab-pane fade container5" id="monthly" style="display:none;"></div>
				</div>				


<script type="text/javascript">
jQuery(function($){
	var url = "<%=request.getContextPath()%>/CountRequest";
	
	$.ajax({
		type: "POST",
		url: url,
		data: "",
		dataType:'json',
		success: function(rtnData) {
			
			$(".nav.nav-tabs").children().each(function() {
				$("#1").click(function(){
					 $("#dayly").show();
					 $("#weekly").hide();
					 $("#monthly").hide();
					 });
				 
                $("#2").click(function(){
                	 $("#dayly").hide();
					 $("#weekly").show();
					 $("#monthly").hide();
					 });

                $("#3").click(function(){
                	 $("#dayly").hide();
					 $("#weekly").hide();
					 $("#monthly").show();
					 });
				 
			});


			
			var day=rtnData.day;
			var resultDay=rtnData.resultDay;
			var resultUserDay=rtnData.resultUserDay;

			var week=rtnData.week;
			var resultWeek=rtnData.resultWeek;
			var resultUserWeek=rtnData.resultUserWeek;

			var month=rtnData.month;
			var resultMonth=rtnData.resultMonth;
			var resultUserMonth=rtnData.resultUserMonth;

			
			var userday=rtnData.userday;
			var userweek=rtnData.userweek;
			var usermonth=rtnData.usermonth;
			console.log("usermonth="+usermonth);
			for(var i=0;i<usermonth.length;i++)
			{
				console.log("usermonth"+i+"="+usermonth[i]);
				}
		
			
			

			

			$('#dayly').highcharts({
	
	            chart: {
	                type: 'column'
	            },
	            title: {
	                text: ''
	            },
	            subtitle: {
	            },
	            xAxis: {
	                categories:  day,	               
	            },
	            yAxis: {
	                min: 0,
	                title: {
	                    text: 'request(number)'
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
		                while(i<day.length)
			                {
                               if(this.x==day[i])
                               {
                            	 
         	                       return s+"<b>Users:</b>"+userday[i];
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
	                data: resultDay,
	    
	            }, {
	                name: 'active users',
	                data: resultUserDay,
	    
	            }]
	            
	        });
			

				
			$('#weekly').highcharts({

	            chart: {
	                type: 'column'
	            },
	            title: {
	                text: ''
	            },
	            subtitle: {
	            },
	            xAxis: {
	                categories: week,
	            },
	            yAxis: {
	                min: 0,
	                title: {
	                    text: 'request(number)'
	                }
	            },
	            tooltip: {
	               /* headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                    '<td style="padding:0"><b>{point.y:.0f} </b></td></tr>',
	                footerFormat: '</table>',
	                shared: true,
	               useHTML: true,*/
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
                        
		                while(i<week.length)
			                {
                               if(this.x==week[i])
                               {
         	                       return s+"<b>Users:</b>"+userweek[i];
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
	                data: resultWeek,
	    
	            }, {
	                name: 'active users',
	                data: resultUserWeek,
	    
	            }]
	        });

			


				
			$('#monthly').highcharts({

	            chart: {
	                type: 'column'
	            },
	            title: {
	                text: ''
	            },
	            subtitle: {
	            },
	            xAxis: {
	                categories: month,
	            },
	            yAxis: {
	                min: 0,
	                title: {
	                    text: 'request(number)'
	                }
	            },
	            tooltip: {
	               /* headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
	                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
	                    '<td style="padding:0"><b>{point.y:.0f} </b></td></tr>',
	                footerFormat: '</table>',
	                shared: true,
	                useHTML: true*/

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
		                while(i<month.length)
			                {
                               if(this.x==month[i])
                               {
                            	 
         	                       return s+"<b>Users:</b>"+usermonth[i];
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
	                data: resultMonth
	    
	            }, {
	                name: 'active users',
	                data: resultUserMonth
	    
	            }]
	        });
			}
			
	});
});
	

		</script>

		</td>
</tr>
</thead>

</table>