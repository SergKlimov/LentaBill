/**
 * Created by artyom on 07.03.16.
 */

$(document).ready(function () {
    google.charts.load("current", {packages:["corechart"]});
    google.charts.setOnLoadCallback(drawBasic);
    function drawBasic() {
        chart = new google.visualization.LineChart(document.getElementById('chart_div'));
        chartForec = new google.visualization.LineChart(document.getElementById('chart_divForec'));
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
    fetchProductList();
    fillProducts();
});

var reportType = {0: "minCheckValue", 1:"v"};
var reportTypeTime={0: "timestamp", 1: "ts"};
var reportTypeSid={0: "storeId", 1: "sid"};
var stores;
var storeIds;

var products;
var productIds;

var productsEnabled;
var productIdsEnabled;

function fetchStoreList() {
    fetchXML("http://localhost:8080/api/meta/storesMeta", {}, function(d) {
        var records = $(d).find("storeMetaRepresentation");
        stores = $(records).map(function() {
            return $(this).find("name").text();
        }).toArray();
        storeIds = $(records).map(function() {
            return Number($(this).find("id").text());
        }).toArray();
    });
}

function fetchProductList() {
    fetchXML("http://localhost:8080/api/meta/products/all", {}, function(d) {
        var records = $(d).find("productRepresentation");
        products = $(records).map(function() {
            return $(this).find("name").text();
        }).toArray();
        productIds = $(records).map(function() {
            return Number($(this).find("id").text());
        }).toArray();

        productsEnabled = products.slice();
        productIdsEnabled = productIds.slice();
    });
}