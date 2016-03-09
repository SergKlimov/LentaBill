function isChecked(checkbox) {
    return $(checkbox)[0].checked;
}

function reportButtonPressed() {
    showTable();
    createReport();
}

function createReport() {
    var byDate = $("#byDate");
    var byStore = $("#byStore");
    var byProduct = $("#byProduct");

    if (isChecked(byProduct)) {
        $("#products").show();
        fillProducts();
    }

    if (isChecked(byDate) && isChecked(byStore) && isChecked(byProduct)) {
        createAllReport();
    } else if (isChecked(byDate) && isChecked(byStore)) {
        createDateStoreReport();
        createDateStoreReportForecast();
    } else if (isChecked(byDate) && isChecked(byProduct)) {
        createDateProductReport();
        //createDateProductReportForecast();
    } else if (isChecked(byStore) && isChecked(byProduct)) {
        createProductStoreReport();
    }
    else {
        alert("Unsupported aggregation type")
    }
}

function fetchXML(url, params, callback) {
    $.ajax({
        url: url,
        method: "GET",
        contentType: "application/xml",
        data: params,
        success: callback
    });
}

function drawPlot(table, plotName, chartObj) {
    data = new google.visualization.DataTable();

    data.addColumn('date', 'Date');
    plotName.forEach(function(value) {
        data.addColumn('number', value);
    });

    table.forEach(function (value) {
        data.addRow(value)
    });
    chartObj.draw(data, chart_options);
}

function drawBars(table, plotName, chartObj) {
    data = new google.visualization.DataTable();

    plotName.forEach(function(value) {
        data.addColumn('number', value);
    });

    table.forEach(function (value) {
        data.addRow(value)
    });
    chartObj = new google.visualization.ColumnChart(document.getElementById('chart_div'));
    chartObj.draw(data, chart_options);
}

function createDateStoreReport() {
    fetchXML(address + "/api/checks/aggregation/byDateAndStore", {since: from*1000, limit: limit}, function (d) {
        var fullDataSet = $(d).find("checksAggregationResultRepresentation");
        var data = prepareData(fullDataSet, reportType, storeIds, "timestamp", "storeId");
        var reportContent = buildTable(data, stores);
        $("#reportTable").html(reportContent);

        drawPlot(data, stores, chart);
    });
}

function createDateStoreReportForecast() {
    fetchXML(address + "/api/checks/forecast/byDateAndStore/min", {since: fromForec, limit: limitForec}, function (d) {
        var fullDataSet = $(d).find("r");
        var data = prepareData(fullDataSet, "v", storeIds, "ts", "sid");
        var reportContent = buildTable(data, stores);
        $("#reportTableForec").html(reportContent);

        drawPlot(data, stores, chartForec);
    });
}

function createDateProductReportForecast() {
    fetchXML(address + "/api/products/0/forecast/value/byDate/min", {since: fromForec, limit: limitForec}, function (d) {
        var fullDataSet = $(d).find("r");
        var data = prepareData2(fullDataSet, "p", storeIds, "ts");
        var reportContent = buildTable(data, stores);
        $("#reportTableForec").html(reportContent);

        drawPlot(data, stores, chartForec);
    });
}

function createAllReport() {
    fetchXML(address + "/api/checks/aggregation/byDateAndStore", {since: from, limit: limit}, function (d) {
        var fullDataSet = $(d).find("checksAggregationResultRepresentation");
        var data = prepareDataByAll(fullDataSet, reportType, storeIds, productIds[0]);
        var reportContent = buildTable(data, stores);
        $("#reportTable").html(reportContent);

        drawPlot(data, stores, chart);
    });
}

function createDateProductReport() {
    fetchXML(address + "/api/products/aggregation/byDateAndStore", {}, function (d) {
        var fullDataSet = $(d).find("productsAggregationByStoreAndDateResultRepresentation");
        var data = prepareData(fullDataSet, reportType, productIdsEnabled, "timestamp", "productId");
        var reportContent = buildTable(data, productsEnabled);
        $("#reportTable").html(reportContent);

        drawPlot(data, productsEnabled, chart);
    });
}

function createProductStoreReport() {
    fetchXML(address + "/api/products/aggregation/byStore", {}, function (d) {
        var fullDataSet = $(d).find("productsAggregationByStoreResultRepresentation");
        var data = prepareDataNoTs(fullDataSet, reportType, storeIds, productIds[0]);
        $("#reportTable").html("Look at the plot");

        drawBars(data, stores, 0);
    });
}

function prepareData(dataList, value, stores, ts_key, store_key) {
    var rawTs = $(dataList).map(function() {
        return Number($(this).find(ts_key).text()); // ts
    });
    var timestamps = $.unique(rawTs).map(function() {
        return new Date(this)
    });

    var list = stores.map(function (store) {
        var dataByStore = $(dataList).filter(function () {
            return Number($(this).find(store_key).text()) == store; // sid
        });
        var res = dataByStore.map(function() {
            return Number($(this).find(value).text())
        });
        return res;
    });
    list.unshift(timestamps); // Prepend
    var table = transpose(list);
    return table;
}

function prepareData2(dataList, value, stores, ts_key) {
    var rawTs = $(dataList).map(function() {
        return Number($(this).find(ts_key).text()); // ts
    });
    var timestamps = $.unique(rawTs).map(function() {
        return new Date(this)
    });

    var res = $(dataList).map(function() {
        return Number($(this).find(value).text())
    });

    var list = [res];

    list.unshift(timestamps); // Prepend
    var table = transpose(list);
    return table;
}

function prepareDataNoTs(dataList, value, stores, product) {
    var list = stores.map(function (store) {
        var dataByStore = $(dataList).filter(function () {
            return Number($(this).find("storeId").text()) == store;
        });

        dataFiltered = $(dataByStore).filter(function () {
            return Number($(this).find("productId").text()) == product;
        });

        var res = dataFiltered.map(function() {
            return Number($(this).find(value).text())
        });
        return res;
    });
    var table = transpose(list);
    return table;
}

function prepareDataByAll(dataList, value, stores, product) {
    var rawTs = $(dataList).map(function() {
        return Number($(this).find("timestamp").text())
    });
    var timestamps = $.unique(rawTs).map(function() {
        return new Date(this)
    });

    var list = stores.map(function (store) {
        var dataByStore = $(dataList).filter(function () {
            return Number($(this).find("storeId").text()) == store;
        });

        dataFiltered = $(dataByStore).filter(function () {
            return Number($(this).find("productId").text()) == product;
        });

        var res = dataFiltered.map(function() {
            return Number($(this).find(value).text())
        });
        return res;
    });
    list.unshift(timestamps); // Prepend
    var table = transpose(list);
    return table;
}

function setType(btn, type) {
    reportType = type;
    $(btn).siblings().each(function () {
        $(this).removeClass("active");
    });
    $(btn).addClass("active");

    createReport();
}

function fillProducts() {
    var productList = new List('products', {
        valueNames: ['name'],
        item: '<li class="list-group-item active" onclick="changeProduct(this)"><p class="name"></p></li>'
    });
    productList.clear();
    products.forEach(function (val) {
        productList.add({
            name: val
        });
    });
    fillProducts = function(){};
}

function changeProduct(elem) {
    var enabled = $(elem).hasClass("active");
    var text = $(elem).text();
    if (enabled) {
        $(elem).attr("list-group-item");

        var pos = productsEnabled.indexOf(text);
        productsEnabled.splice(pos, 1);
        productIdsEnabled.splice(pos, 1);
    } else {
        $(elem).attr("list-group-item active");

        productsEnabled.push(text);
        var pos = products.indexOf(text);
        var id = productIds[pos];
        productIdsEnabled.push(id);
    }

    createReport();
}