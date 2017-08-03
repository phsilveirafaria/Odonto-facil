angular.module('odontoFacil').factory('loginFactory',['$http', function($http) {
	
	var _autenticar = function(cliente) {
		return $http.post(
				'http://localhost:8080/autenticar',
				cliente
		);
	};
	
	return {
		autenticar: _autenticar
	}
}]);