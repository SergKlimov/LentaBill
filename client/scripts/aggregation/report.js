function isChecked(checkbox) {
  return $(checkbox)[0].checked;
}

function createReport() {
  var byDate = $("#byDate");
  var byStore = $("#byStore");
  var byProduct = $("#byProduct");

  if (isChecked(byProduct)) {
      $("#products").show();
  }
  
  if (isChecked(byDate) && isChecked(byStore)) {
    createDateStoreReport();
  } else {
    alert("Unsupported aggregation type")
  }
}

function buildChecksAggregationViewsContent(dataList, selector) {
  var table = buildTable(dataList, selector);
    return table;
}

function buildChecksAggregationViews(dataList) {
  return buildChecksAggregationViewsContent(dataList, "minCheckValue");
}

function fetchAggregationXML(url, callback) {
  $.ajax({
    url: address + "/api/checks/aggregation/byDateAndStore",
    method: "GET",
    contentType: "application/xml",
    data: {since: from , limit: limit},
    success: callback
  });
}

function drawPlot(dataList) {
        data = new google.visualization.DataTable();

        data.addColumn('date', 'Date');
        data.addColumn('number', 'Check value');

        var dataForStore = $(dataList).filter(function() {
            return Number($(this).find("storeId").text()) == 0;
        });

        dataForStore.each(function() {
            var timestamp = Number($(this).find("timestamp").text());
            var value = Number($(this).find("minCheckValue").text());
            data.addRow([new Date(timestamp), value])
        });

        chart.draw(data, chart_options);
}

function createDateStoreReport() {
    fetchAggregationXML(address + "/api/checks/aggregation/byDateAndStore", function (d) {
        var fullDataSet = $(d).find("checksAggregationResultRepresentation");
        var reportContent = buildChecksAggregationViews(fullDataSet);
        $("#reportTable").html(reportContent);
        drawPlot(fullDataSet);

        $("#ex2").slider({});
    });
}

$(document).ready(function() {
  $("#createReportButton").click(createReport);
});