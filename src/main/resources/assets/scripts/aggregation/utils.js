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
  for(i = 1; i < contentArray.length; ++i) {
    accumulator += (separator + contentArray[i]);
  }
  return accumulator;
}

function byTimestampComparator(o1, o2) {
  ts1 = Number($(o1).find("timestamp").text());
  ts2 = Number($(o2).find("timestamp").text());
  return ts1 - ts2;
}

function toHumanReadableDate(date) {
  var year = date.getFullYear();
  var month = "0" + (date.getMonth() + 1);
  var day = "0" + date.getDate();
  var humanReadableDate = day.substr(-2) + '.' + month.substr(-2) + "." + year;
  return humanReadableDate;
}

function toMilliseconds(dateString) {
  var match = /^(\d\d)\.(\d\d)\.(\d{4})/.exec(dateString);
  var day = Number(match[1])
  var month = Number(match[2]) - 1
  var year = Number(match[3])
  return new Date(year, month, day, 0, 0, 0).getTime();
}

function addDays(timestampMs, daysToAdd) {
  return timestampMs + daysToAdd * 24 * 60 * 60 * 1000;
}

function transpose(a) {
  // Calculate the width and height of the Array
  var w = a.length ? a.length : 0,
      h = a[0].length;

  // In case it is a zero matrix, no transpose routine needed.
  if(h === 0 || w === 0) { return []; }

  /**
   * @var {Number} i Counter
   * @var {Number} j Counter
   * @var {Array} t Transposed data is stored in this array.
   */
  var i, j, t = [];
  // Loop through every item in the outer array (height)
  for(i=0; i<h; i++) {
    // Insert a new row (array)
    t[i] = [];
    // Loop through every item per item in outer array (width)
    for(j=0; j<w; j++) {
      // Save transposed data.
      t[i][j] = a[j][i];
    }
  }

  return t;
}