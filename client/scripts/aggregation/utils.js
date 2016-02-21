function wrapByTag(content, tag, classes) {
  var classValue;
  if ($.isArray(classes)) {
    classValue = concatArray(classes, " ");
  } else {
    classValue = classes;
  }
  var classAttribute = classValue == "" ? "" : "class=\"" + classValue + "\"";
  return "<" + tag + " " + classAttribute + ">" + content + "</" + tag + ">";
}

function concatArray(contentArray, separator) {
  if (contentArray.length == 0) {
    return "";
  }
  var accumulator = contentArray[0];
  for(i = 0; i < contentArray.length; ++i) {
    accumulator += (separator + contentArray[i]);
  }
  return accumulator;
}

function byTimestampComparator(o1, o2) {
  ts1 = Number($(o1).find("timestamp").text());
  ts2 = Number($(02).find("timestamp").text());
  return ts1 - ts2;
}

function toHumanReadableDate(timestampMs) {
  var date = new Date(timestampMs);
  var year = date.getFullYear();
  var month = "0" + (date.getMonth() + 1);
  var day = "0" + date.getDate();
  var humanReadableDate = day.substr(-2) + '.' + month.substr(-2) + "." + year;
  return humanReadableDate;
}