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
    fetchStoreList();
});

var reportType = "minCheckValue";
var stores;
var storeIds;

function fetchStoreList() {
    fetchXML("http://localhost:8080/api/meta/storesMeta", {}, function(d) {
        var records = $(d).find("storeMetaRepresentation");
        stores = $(records).map(function() {
            return $(this).find("name").text();
        });
        storeIds = $(records).map(function() {
            return Number($(this).find("id").text());
        });
    });
}