angular.module('odontoFacil').factory('clienteFactory',['$http', function($http) {
	
	var _cliente;
	var _editandoCliente;
	
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
		return $http.get(
			'http://localhost:8080/excluirClientes/',
			cliente.idCliente
		);
	};
	
	return {
		getCliente: function() { return cliente; },
		setCliente: function(cliente) { cliente = cliente; },
		isEditandoCliente: function() { return _editandoCliente; },
		setEditandoClientes: function(editandoCliente) { _editandoCliente = editandoCliente; },
		listarClientes: _listarClientes,
		salvarClientes: _salvarClientes,
		excluirClientes: _excluirClientes
	}
}]);