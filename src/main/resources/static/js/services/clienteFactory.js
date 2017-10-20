angular.module('odontoFacil').factory('clienteFactory',['$http', function($http) {
	
	var _cliente;
	var _editandoCliente;
	
	var _listarClientes = function() {
		return $http.get(
			'https://localhost:8443/listarClientes'
		);
	};
	
	var _salvarClientes = function(cliente) {
		return $http.post(
			'https://localhost:8443/salvarClientes',
			cliente
		);
	};
	
	var _excluirClientes = function(cliente) {
		return $http.post(
			'https://localhost:8443/excluirClientes/',
			cliente
		);
	};
	
	return {
		getCliente: function() { return _cliente; },
		setCliente: function(cliente) { _cliente = cliente; },
		isEditandoCliente: function() { return _editandoCliente; },
		setEditandoCliente: function(editandoCliente) { _editandoCliente = editandoCliente; },
		listarClientes: _listarClientes,
		salvarClientes: _salvarClientes,
		excluirClientes: _excluirClientes
	}
}]);