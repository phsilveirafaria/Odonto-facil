angular.module('odontoFacil').controller('modalAgendamentoController', ['$scope', 
	'$location', '$mdDialog', 'agendamentoFactory', 
	function ($scope, $location, $mdDialog,	agendamentoFactory) {
	
	var ctrl = this;	
	
	ctrl.agendamentoCarregado = agendamentoFactory.getAgendamentoCarregado();
	ctrl.agendamento = agendamentoFactory.getAgendamento();
	
	agendamentoFactory.listarClientes().then(
			sucessCallback = function(response){
				ctrl.comboClientes = response.data;
			},
			errorCallback = function (error){
				
			});
	
	agendamentoFactory.listarFuncionarios().then(
			sucessCallback = function(response){
				ctrl.comboFuncionarios = response.data;
			},
			errorCallback = function (error){
				
			});
	
	 /**
	  * Atualiza o campo descricao do agendamento
	  */
	 var updateTitle = function (agendamento) {		
		 return agendamento.descricao ? agendamento.cliente.nomeCompleto + " (" +
				 agendamento.descricao + ")" : agendamento.cliente.nomeCompleto;			  
	 };
	 
	ctrl.salvar = function (agendamento) {

		agendamentoFactory.salvarAgendamento(agendamento).then(
					successCallback = function(response) {	  				   					
						var event = angular.element('.calendar').fullCalendar('clientEvents',agendamento.id);																								
						if (event.length > 0) {
							agendamento.title = updateTitle(agendamento);
						} function errorCallback(response) {								
							$mdDialog.show(
								$mdDialog.alert()
									.clickOutsideToClose(true)
									.title('Algo saiu errado ...')
									.textContent("Não foi possível encontrar o agendamento com o id informado!")
									.ariaLabel('Alerta')
									.ok('Ok')						
							);
							
						}	

		// Novo agendamento
		if (agendamento.paciente) {						
			agendamento.title = updateTitle(agendamento);
			var horarioConsulta = agendamento.formatedStart.split(":");
			agendamento.start = moment(agendamento.start).hour(horarioConsulta[0]).minute(horarioConsulta[1]).second(0).millisecond(0);			
			agendamento.end = moment(agendamento.start).add(ctrl.tempoSessao, 'm');
			agendamento.ativo = true;
					
			
			agendamentoFactory.salvarAgendamento(agendamento).then(
					successCallback = function(response) {
						angular.element('.calendar').fullCalendar('renderEvent', response.data);
					},
					errorCallBack = function(error) {
						//utilService.hideWait();			
						utilService.tratarExcecao(error).then(function() {
							var view = angular.element('.calendar').fullCalendar('getView');
							listarAgendamento(view.start, view.end);
						});
					}
			);																
		};								
		
		angular.element('.calendar').fullCalendar('unselect');												
	};			
	
	}	
}]);
