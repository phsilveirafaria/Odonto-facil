angular.module('odontoFacil').factory('dashboardFactory',['$http', function($http) {
	var _funcionario;
	
	var ctrl = this;
	
	
	var _listarValores = function() {
		return $http.get('https://localhost:8443/listarValoresPorFuncionario');
	};
	
	var _listarAgendamentosDoMesPorFuncionario = function() {
		return $http.get('https://localhost:8443/listarValoresPorFuncionario');
	};
	
	return {
		getFuncionario: function() { return _funcionario; },
		setFuncionario: function(funcionario) { _funcionario = funcionario; },
		listarValores: _listarValores,
		listarAgendamentosDoMesPorFuncionario: _listarAgendamentosDoMesPorFuncionario
	}
}]);