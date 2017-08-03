angular.module('odontoFacil').factory('clienteFactory',['$http', function($http) {
	
	var _listarClientes = function() {
		return $http.get(
			'http://localhost:8080/listarClientes'
		);
	};
	
	var _salvarClientes = function(cliente) {
		return $http.post(
			'http://localhost:8080/salvarClientes',
			cliente
		);
	};
	
	var _excluirClientes = function(cliente) {
		return $http.delete(
			'http://localhost:8080/excluirClientes/'+ cliente.idCliente,
			cliente
		);
	};
	
	return {
		listarClientes: _listarClientes,
		salvarClientes: _salvarClientes,
		excluirClientes: _excluirClientes
	}
}]);