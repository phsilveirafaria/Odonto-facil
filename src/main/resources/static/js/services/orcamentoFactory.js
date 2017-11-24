angular.module('odontoFacil').factory('orcamentoFactory',['$http', function($http) {
	
	var _orcamento;
	var _editandoOrcamento;
	
	var _listarOrcamentos = function() {
		return $http.get(
			'https://localhost:8443/listarOrcamentos'
		);
	};
	
	var _salvarOrcamentos = function(orcamento) {
		return $http.post(
			'https://localhost:8443/salvarOrcamentos',
			orcamento
		);
	};
	
	var _salvarArquivo = function(arquivo) {
		return $http.post(
			'https://localhost:8443/api/upload/multi/model',
			arquivo
		);
	};
	
	var _listarClientes = function() {
		return $http.get(
			'https://localhost:8443/listarClientes'
		);
	};
	
	var _listarFuncionarios = function() {
		return $http.get(
			'https://localhost:8443/listarFuncionarios'
		);
	};
	
	var _imprimirRelatorioOrcamento = function(orcamento) {
		return $http.post(
			'https://localhost:8443/imprimirRelatorioOrcamento',
			orcamento, {responseType:'arraybuffer'}
		);
	};
	
	var _excluirOrcamentos = function(orcamento) {
		return $http.post(
			'https://localhost:8443/excluirOrcamentos/',
			orcamento
		);
	};
	
	return {
		getOrcamento: function() { return _orcamento; },
		setOrcamento: function(orcamento) { _orcamento = orcamento; },
		listarOrcamentos: _listarOrcamentos,
		isEditandoOrcamento: function() { return _editandoOrcamento; },
		setEditandoOrcamento: function(editandoOrcamento) { _editandoOrcamento = editandoOrcamento; },
		salvarOrcamentos: _salvarOrcamentos,
		salvarArquivo: _salvarArquivo,
		imprimirRelatorioOrcamento: _imprimirRelatorioOrcamento,
		listarFuncionarios: _listarFuncionarios,
		listarClientes: _listarClientes,
		excluirOrcamentos: _excluirOrcamentos
	}
}]);