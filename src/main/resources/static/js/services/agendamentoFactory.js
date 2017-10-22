angular.module('odontoFacil').factory('agendamentoFactory', ['$http',  function($http) {
	var _agendamento = {};
	var _agendamentoCarregado = {};	
	
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
	
	var _agendamentosDoMes = function() {
		return $http.get(
			'https://localhost:8443/agendamentosDoMes');
	};
	
	var _listarAgendamentos = function(dataInicial, dataFinal) {
		var params = {dataInicial: dataInicial.format(), dataFinal: dataFinal.format()};
		return $http.get('https://localhost:8443/listarAgendamentos', {params});
	};
	
	var _salvarAgendamento = function(ag) {
		var agendamento = angular.copy(ag);
		return $http.post('https://localhost:8443/salvarAgendamento', angular.copy(agendamento));
	};
	
	var _removerAgendamento = function(agendamento) {
		return $http.post('https://localhost:8443/removerAgendamento', angular.copy(agendamento));
	};
	
	return {		
		getAgendamento: function() { return _agendamento; },
		setAgendamento: function(agendamento) { _agendamento = agendamento; },		
		getStart: function() { return _agendamento.start; },
		setStart: function(start) { _agendamento.start = start; },
		getEnd: function() { return _agendamento.end; },
		setEnd: function(end) { _agendamento.end = end; },
		salvarAgendamento: _salvarAgendamento,
		listarClientes: _listarClientes,
		listarAgendamentos: _listarAgendamentos,
		removerAgendamento: _removerAgendamento,
		agendamentosDoMes: _agendamentosDoMes,
		listarFuncionarios: _listarFuncionarios,
		getAgendamentoCarregado: function() { return _agendamentoCarregado; },
		setAgendamentoCarregado: function(agendamentoCarregado) { _agendamentoCarregado = agendamentoCarregado; },
		getEditable: function() { return _agendamento.editable; },
		setEditable: function(editable) { _agendamento.editable = editable; },
		getFormatedStart: function() { return _agendamento.formatedStart; },
		setFormatedStart: function(formatedStart) { _agendamento.formatedStart = formatedStart; },
	};
}]);