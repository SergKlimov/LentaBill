address = "http://localhost:8080"

dataDomain = {};
storesMeta = [];

$.ajax({
  url: address + "/api/meta/dataCollectionDomain",
  method: "GET",
  contentType: "application/xml",
  success: function(d) {
    var domain = $(d).find("dataCollectionDomainRepresentation")
    dataDomain.lb = Number($(domain).find("lowerBound").text());
    dataDomain.ub = Number($(domain).find("upperBound").text());
  }
});

$.ajax({
  url: address + "/api/meta/storesMeta",
  method: "GET",
  contentType: "application/xml",
  success: function(d) {
    $(d)
      .find("storeMetaRepresentation")
      .each(function() {
        var storeMeta = {
          id: Number($(this).find("id").text()),
          name: $(this).find("name").text()
        };
        storesMeta.push(storeMeta);
      });
  }
});