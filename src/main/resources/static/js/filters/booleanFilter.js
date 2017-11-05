angular.module('odontoFacil').filter('booleanFormat', function() {
    return function(valor) {
    	return valor?"Sim":"Não";   	
    };
});