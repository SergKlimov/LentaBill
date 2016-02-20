var address = "http://localhost:8080";

var isChecked = function(checkbox) {
  return checkbox.prop("checked");
}

var wrapByTag = function(content, tag) {
  return "<" + tag + ">" + content + "</" + tag + ">"
}

var toHumanReadableDate = function(timestampMs) {
  var date = new Date(timestampMs);
  var year = date.getFullYear();
  var month = "0" + (date.getMonth() + 1);
  var day = "0" + date.getDate();
  var formattedTime = day.substr(-2) + '.' + month.substr(-2) + "." + year;
  return formattedTime;
}

$(document).ready(function() {
  var byDateCheckbox = $("#byDate")
  var byStoreCheckbox = $("#byStore")
  var byProductCheckbox = $("#byProduct")

  //var byDateResultsTable
  //byDateResultsTable = $("#byDateResultsTable").DataTable();

  $("#createReportButton").click(function() {
    if (isChecked(byDateCheckbox) && isChecked(byStoreCheckbox)) {

      $.ajax({
        url: address + "/api/checks/aggregation/byDateAndStore",
        method: "GET",
        contentType: "application/xml",
        data: {since: Date.now(), limit: 8},
        success: function (d) {
          data = $(d)
            .find("checksAggregationResultRepresentation")
            .sort(function(r1, r2) {
             ts1 = parseInt($(r1).find("timestamp").text(), 10)
             ts2 = parseInt($(r2).find("timestamp").text(), 10)
             return ts1 - ts2
            })
          headers = wrapByTag("", "th")
          results = $.unique(
            $(data).map(function() {
               return $(this).find("timestamp").text()
             })
            ).map(function() {
              return toHumanReadableDate(parseInt(this, 10))
            }).each(function() {
              headers += wrapByTag(this, "th")
            });
          content = ""
          for (i = 0; i < 5; ++i) {
            row = wrapByTag(i, "td")
            $(data).each(function() {
              sidText = $(this).find("storeId").text()
              if (parseInt(sidText, 10) == i) {
                row += wrapByTag($(this).find("minCheckValue").text(), "td")
              }
            });
            content += wrapByTag(row, "tr")
          }
          table = "<table class=\"table striped hovered cell-hovered bordered\">" +
            wrapByTag(wrapByTag(headers, "tr"), "thead") +
            wrapByTag(content, "tbody") +
            "</table>"
          $("#byDateResults").html(table)
          /*$(data)
            .find("checksAggregationResultRepresentation")
            .each(function() {
              byDateResultsTable
                .row
                .add( [
                  $(this).find("timestamp").text(),
                  $(this).find("storeId").text(),
                  $(this).find("minCheckValue").text()
                ]).draw( false );
              });*/
        }
      });
    }
  });
});