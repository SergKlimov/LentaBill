/**
 * Created by artyom on 07.03.16.
 */

$(document).ready(function () {
    var userList = new List('users', {valueNames: ['name', 'born']});
    google.charts.load("current", {packages:["corechart"]});
    google.charts.setOnLoadCallback(drawBasic);
    function drawBasic() {
        chart = new google.visualization.LineChart(document.getElementById('chart_div'));
         chart_options = {
            hAxis: {
                title: 'Time'
            },
            vAxis: {
                title: 'Popularity'
            }
        };
        data = new google.visualization.DataTable();
    }
});

$(document).ready(function() {
    $("#createReportButton").click(reportButtonPressed);
});

var reportType = "minCheckValue";