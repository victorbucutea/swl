var transformToArray = function(items) {
	var itemsDataGrig = [];
	if (items.nl != undefined) {
		itemsDataGrig = itemsDataGrig.concat(items.nl);
	}
	if (items.fr != undefined) {
		itemsDataGrig = itemsDataGrig.concat(items.fr);
	}
	if (items.en != undefined) {
		itemsDataGrig = itemsDataGrig.concat(items.en);
	}
	if (items.de != undefined) {
		itemsDataGrig = itemsDataGrig.concat(items.de);
	}
	if (items.unknown != undefined) {
		itemsDataGrig = itemsDataGrig.concat(items.unknown);
	}
	return itemsDataGrig;
}

var determineName = function(items) {
	if (items["nl"] != undefined) {
		return items["nl"][0].text;
	}
	if (items["fr"] != undefined) {
		return items["fr"][0].text;
	}
	if (items["en"] != undefined) {
		return items["en"][0].text;
	}
	if (items["de"] != undefined) {
		return items["de"][0].text;
	}
	if (items["unknown"] != undefined) {
		return items["unknown"][0].text;
	}
	return "";
}


var getTranslationElementsByFlag = function getElementsByFlag(items, flag) {
	var results = new Array();
	for ( var int = 0; int < items.length; int++) {
		var item = items[int];
		if (item[flag]) {
			results.push(item);
		}
	}
	return results;
};

var addNamesCollectionsToScope = function(entity, namesArray) {
	var namesArray = $.extend(true, [], namesArray);
	var names_added = getTranslationElementsByFlag(namesArray, "created");
	if (names_added != null) {
		entity.added_names = names_added;
	}
	var names_updated = getTranslationElementsByFlag(namesArray, "updated");
	if (names_updated != null) {
		entity.updated_names = names_updated;
	}
	var names_removed = getTranslationElementsByFlag(namesArray, "removed");
	if (names_removed != null) {
		entity.removed_names = names_removed;
	}
}
var addSynonymsCollectionsToScope = function(entity, synonymsArray) {
	var synsArray = $.extend(true, [], synonymsArray);
	var syns_added = getTranslationElementsByFlag(synsArray, "created");
	if (syns_added != null) {
		entity.added_synonyms = syns_added;
	}
	var syns_updated = getTranslationElementsByFlag(synsArray, "updated");
	if (syns_updated != null) {
		entity.updated_synonyms = syns_updated;
	}
	var syns_removed = getTranslationElementsByFlag(synsArray, "removed");
	if (syns_removed != null) {
		entity.removed_synonyms = syns_removed;
	}
}

var createNamesToScope = function(entity, arrayOfNames) {
	var namesArray = $.extend(true, [], arrayOfNames);
	var names_added = getTranslationElementsByFlag(namesArray, "created");
	if (names_added != null  && names_added.length>0) {
		entity.names = names_added;
	}
};

var createSynonymsToScope = function(entity, arrayOfSynonyms) {
	var synsArray = $.extend(true, [], arrayOfSynonyms);
	var syns_added = getTranslationElementsByFlag(synsArray, "created");
	if (syns_added != null && syns_added.length>0) {
		entity.synonyms = syns_added;
	}
};

var createErrorMessage = function(errorData) {
	var errorMessage = 'Error key: ' + errorData.key;
    if (errorData.id) {
        errorMessage = errorMessage + ' id: '+ errorData.id;
    }
	if (errorData.parameters != undefined && errorData.parameters.length > 0) {
		errorMessage += ', With parameters: ';
		for (var i = 0; i < errorData.parameters.length; i++) {
			errorMessage += errorData.parameters[i] + ', ';
		}
	}
	return errorMessage;
}
