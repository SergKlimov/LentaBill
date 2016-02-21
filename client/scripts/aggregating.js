var address = "http://localhost:8080";
var limit = 8

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

var toMilliseconds = function(dateString) {
  var match = /^(\d\d)\.(\d\d)\.(\d{4})/.exec(dateString);
  var day = Number(match[1])
  var month = Number(match[2]) - 1
  var year = Number(match[3])
  return new Date(year, month, day, 0, 0, 0).getTime();
}

buildReport = function(since) {
  $.ajax({
    url: address + "/api/checks/aggregation/byDateAndStore",
    method: "GET",
    contentType: "application/xml",
    data: {since: since, limit: limit},
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
          })).map(function() {
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
          $("#reportView").html(table)
        }
  });
}

var reportControlDiv;

var updateStartReportTimestamp = function(v, slider) {
  var startReportTimestampInput = reportControlDiv.find("#startReportTimestamp");
  var currentStartReportTimestamp = startReportTimestampInput.val();
  if (currentStartReportTimestamp != toHumanReadableDate(v)) {
    startReportTimestampInput.val(toHumanReadableDate(v))
    var d = new Date(v);
    var truncated = new Date(d.getFullYear(), d.getMonth(), d.getDate(), 0, 0, 0)
    updateReport(toMilliseconds(currentStartReportTimestamp), truncated.getTime())
  }
}

var updateReport = function(oldStartReportTimestamp, currentStartReportTimestamp) {
  var timeDiff = Math.abs(oldStartReportTimestamp - currentStartReportTimestamp)
  var diffDays = Math.floor(timeDiff / (1000 * 60 * 60 * 24))
  if (diffDays >= limit - 1) {
    buildReport(currentStartReportTimestamp)
  } else {
    var reportTable = $("#reportView").find(".table")
    if (currentStartReportTimestamp > oldStartReportTimestamp) {

      var headers = reportTable.find("th");
      for(i = diffDays + 1; i < headers.length; ++i) {
       $(headers[i - diffDays]).text($(headers[i]).text())
      }
      reportTable.find("tbody").find("tr")
        .each(function() {
          var cells = $(this).find("td");
          for(i = diffDays + 1; i < cells.length; ++i) {
            $(cells[i - diffDays]).text($(cells[i]).text())
          }
        })

      $.ajax({
        url: address + "/api/checks/aggregation/byDateAndStore",
        method: "GET",
        contentType: "application/xml",
        data: {
          since: currentStartReportTimestamp + (limit - diffDays) * 24 * 60 * 60 * 1000,
          limit: diffDays},
        success: function (d) {
          var data = $(d)
            .find("checksAggregationResultRepresentation")
            .sort(function(r1, r2) {
              var ts1 = parseInt($(r1).find("timestamp").text(), 10)
              var ts2 = parseInt($(r2).find("timestamp").text(), 10)
              return ts1 - ts2
            })
          var originUpdates = $.unique(
            $(data).map(function() {
              return $(this).find("timestamp").text()
            })).map(function() {
              return toHumanReadableDate(Number(this))
            })
          var headers = reportTable.find("th");
          for(i = 0; i < originUpdates.length; ++i) {
            $(headers[headers.length - diffDays + i]).text(originUpdates[i])
          }
          rows = reportTable.find("tbody").find("tr")
          data.each(function() {
            var rowId = Number($(this).find("storeId").text());
            var row = rows[rowId]
            var origin = Number($(this).find("timestamp").text());
            var daysTill = Math.floor(
              (origin - currentStartReportTimestamp) / (1000 * 24 * 60 * 60))
            $($(row).find("td")[daysTill + 1]).text($(this).find("minCheckValue").text())
          })
        }
      })

    } else {

      var headers = reportTable.find("th");
      for(i = headers.length - 1 - diffDays; i > 0; --i) {
        $(headers[i + diffDays]).text($(headers[i]).text())
      }
      reportTable.find("tbody").find("tr")
        .each(function() {
          var cells = $(this).find("td");
          for(i = headers.length - 1 - diffDays; i > 0; --i) {
            $(cells[i + diffDays]).text($(cells[i]).text())
          }
        })

        $.ajax({
          url: address + "/api/checks/aggregation/byDateAndStore",
          method: "GET",
          contentType: "application/xml",
          data: {
            since: currentStartReportTimestamp,
            limit: diffDays},
          success: function (d) {
            var data = $(d)
              .find("checksAggregationResultRepresentation")
              .sort(function(r1, r2) {
                var ts1 = parseInt($(r1).find("timestamp").text(), 10)
                var ts2 = parseInt($(r2).find("timestamp").text(), 10)
                return ts1 - ts2
              })
              var originUpdates = $.unique(
                $(data).map(function() {
                  return $(this).find("timestamp").text()
                })).map(function() {
                  return toHumanReadableDate(Number(this))
                })
              var headers = reportTable.find("th");
              for(i = 0; i < originUpdates.length; ++i) {
                $(headers[i + 1]).text(originUpdates[i])
              }
              rows = reportTable.find("tbody").find("tr")
              data.each(function() {
                var rowId = Number($(this).find("storeId").text());
                var row = rows[rowId]
                var origin = Number($(this).find("timestamp").text());
                var daysTill = Math.floor(
                  (origin - currentStartReportTimestamp) / (1000 * 24 * 60 * 60))
                $($(row).find("td")[daysTill + 1]).text($(this).find("minCheckValue").text())
              })
            }
          })
    }
  }
}

$(document).ready(function() {
  var byDateCheckbox = $("#byDate")
  var byStoreCheckbox = $("#byStore")
  var byProductCheckbox = $("#byProduct")

  reportControlDiv = $("#reportControl")

  var dataDomain;
  $.ajax({
    url: address + "/api/meta/dataCollectionDomain",
    method: "GET",
    contentType: "application/xml",
    success: function(d) {
      var domain = $(d).find("dataCollectionDomainRepresentation")
      dataDomain = {
        lb: parseInt($(domain).find("lowerBound").text(), 10),
        ub: parseInt($(domain).find("upperBound").text(), 10)
      }
    }
  });

  $("#createReportButton").click(function() {
    if (isChecked(byDateCheckbox) && isChecked(byStoreCheckbox)) {
      buildReport(dataDomain.lb, 8)

      reportControlDiv.html("<div class=\"input-control text\"> " +
        "<input " +
        "id=\"startReportTimestamp\" " +
        /*"onchange=\"buildReport_noArgs()\" " +*/
        "value=\"" + toHumanReadableDate(dataDomain.lb) + "\" " +
        "readonly> " +
        "</div> " +
        "<div class=\"slider large\" data-role=\"slider\" " +
        "data-min-value=" + dataDomain.lb + " " +
        "data-max-value=" + dataDomain.ub + " " +
        "data-accuracy=" + (100 * (24 * 60 * 60 * 1000) / (dataDomain.ub - dataDomain.lb)) + " " +
        "data-on-change=updateStartReportTimestamp " +
        "></div>"
        )
      reportControlDiv.show()
    }
  });
});