function isChecked(checkbox) {
  return $(checkbox)[0].checked;
}

function injectReportView() {
  var byDate = $("#byDate");
  var byStore = $("#byStore");
  var byProduct = $("#byProduct");
  
  if (isChecked(byDate) && isChecked(byStore)) {
    injectChecksReportView();
  } else if (isChecked(byDate) && isChecked(byProduct)) {
    alert("byDataAndProduct");
  } else if (isChecked(byStore) && isChecked(byProduct)) {
    alert("byStoreAndProduct");
  } else if (isChecked(byProduct) && isChecked(byStore) && isChecked(byProduct)) {
    alert("byDataAndStoreAndProduct");
  } else {
    //show error and tips orm smth like this
  }
}

function wrapAggregationView(aggregationView) {
  var div_classList =  "<div class=\"list\" style=\"height:auto\">" + aggregationView + "</div>";
  return "<div class=\"list-group-content\" style=\"display:none\">" + div_classList + "</div>";
}

function buildAggregationView(title, aggregationView) {
  var wrappedTitle = wrapByTag(title, "span", ["list-group-toggle", "sub-header"]);
  var wrappedAggregationView = wrapAggregationView(aggregationView);
  return wrapByTag(wrappedTitle + wrappedAggregationView, "div", ["list-group", "collapsed"]);
}

function buildChecksAggregationViewsContent(dataList, selector) {
  var switcher = buildSwitcher();
  var table = buildTable(dataList, selector);
  var wrappedTable = wrapByTag(table, "div", "aggregation-table-view");
  var graph = buildGraph(dataList, selector);
  var wrappedGraph = "<div " +
    "class=\"aggregation-graph-view\" style=\"display: none\">" +
    graph +
    "</div>";
  var control = buildController();
  var content = switcher + wrappedTable + wrappedGraph + control;
  return "<div " +
    "class=\"aggregation-view-content\" " +
    "selector=\"" + selector +"\" " +
    "mode=\"table\" " +
    ">" + content + "</div>";
  //return wrapByTag(table + control, "div", "");
}

function buildChecksAggregationViews(dataList) {
  var minCheckValueView = buildChecksAggregationViewsContent(dataList, "minCheckValue");
  var avgCheckValueView = buildChecksAggregationViewsContent(dataList, "avgCheckValue");
  var maxCheckValueView = buildChecksAggregationViewsContent(dataList, "maxCheckValue");
  var checksValueSumView = buildChecksAggregationViewsContent(dataList, "allChecksValueSum");
  var checksCountView = buildChecksAggregationViewsContent(dataList, "checksCount");
  var av1 = buildAggregationView("Minimum check value.", minCheckValueView);
  var av2 = buildAggregationView("Maximum check value.", maxCheckValueView);
  var av3 = buildAggregationView("Average check value.", avgCheckValueView);
  var av4 = buildAggregationView("Checks sum value.", checksValueSumView);
  var av5 = buildAggregationView("Checks count.", checksCountView);
  return av1 + av2 + av3 + av4 + av5;
}

function injectChecksReportView() {
  $.ajax({
    url: address + "/api/checks/aggregation/byDateAndStore",
    method: "GET",
    contentType: "application/xml",
    data: {since: dataDomain.lb, limit: 7},
    success: function (d) {
      var fullDataSet = $(d).find("checksAggregationResultRepresentation");
      var av = buildChecksAggregationViews(fullDataSet);
      $("#reportView").html(av);
    }
  });

}

$(document).ready(function() {
  $("#createReportButton").click(injectReportView);
})