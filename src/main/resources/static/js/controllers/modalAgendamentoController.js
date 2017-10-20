var lazyModules = ['ui.calendar', 'ui.bootstrap']; 
angular.forEach(lazyModules, function(dependency) {
	angular.module('odontoFacil').requires.push(dependency);
});

angular.module('odontoFacil').controller('modalAgendamentoController', ['$scope', '$http',
	'$location', '$mdDialog', 'agendamentoFactory', 'Session', '$uibModal' , '$uibModalInstance', 'modalAgendamentoService', 
	function ($scope, $http, $location, $mdDialog,	agendamentoFactory, Session, $uibModal, $uibModalInstance, modalAgendamentoService) {
	
	var ctrl = this;		
	ctrl.x = Session.usuario;
	
	ctrl.agendamentoCarregado = agendamentoFactory.getAgendamentoCarregado();
	ctrl.agendamento = agendamentoFactory.getAgendamento();
	
	
	ctrl.funcionarioLogado = function(x) {
		/*homeFactory.funcionarioLogado(nomeFuncionario).then(function successCallback(response){
		Session.create(response.data);
		ctrl.funcionario = response.data;
		return ctrl.funcionario;
	}, function errorCallback(response) {
		console.log(response.data);
		console.log(response.status);
	});*/
		$http({
			  method: 'GET',
			  url: 'https://localhost:8443/userLogado/'
			}).then(function successCallback(response) {
			    Session.create(response.data);
			    ctrl.x = response.data;
			  }, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			  });
		
	}
	ctrl.funcionarioLogado();
	
	
	agendamentoFactory.listarClientes().then(
			sucessCallback = function(response){
				ctrl.comboClientes = response.data;
			},
			errorCallback = function (error){
				
			});
	
	ctrl.iniciarConsulta = function(agendamento) {		
		consultaFactory.setAgendamento(agendamento);
		if (agendamento.consulta) {						
			$location.path('/consulta');
		} else {
			if (agendamento.cliente) {								
				consultaPacienteFactory.setConsulta({});
				consultaPacienteFactory.setId(null);				
				consultaPacienteFactory.setProntuario(null);
				consultaPacienteFactory.setValor(0);
				consultaPacienteFactory.setRecibo(false);
				consultaPacienteFactory.setInicio(new Date());
				consultaPacienteFactory.setFim(null);
				$location.path('/consulta');
			} else {
				$mdDialog.show(
					$mdDialog.alert()
						.clickOutsideToClose(true)
						.title('Algo saiu errado ...')
						.textContent("Não foi possível localizar o paciente da consulta!")
						.ariaLabel('Alerta')
						.ok('Ok')						
				);							
			}
		}
		$uibModalInstance.close();			
	};
	
	
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
	 
		ctrl.cancel = function () {				
			$uibModalInstance.dismiss('cancel');				
		};	
	 
	 
	 /**
		 * Confirma com o usuário a remoção do evento
		 */
		ctrl.confirmarRemocaoEvento = function (agendamento) {		
			modalAgendamentoService.openConfirmModal();
			
			$uibModalInstance.close();
		};	
		
		var removerEvento = function(agendamento) {	
			//utilService.setMessage("Removendo agendamento ...");
			//utilService.showWait();		
			agendamentoFactory.removerAgendamento(agendamento).then(
					successCallback = function(response) {					
						if (agendamento.grupo > 0 && agendamento.eventoPrincipal) {
							agendamentoFactory.atribuirNovoEventoPrincipal(agendamento).then(
									successCallback = function(response) {
										//utilService.hideWait();
										angular.element('.calendar').fullCalendar('removeEvents',agendamento.id);				
									},
									errorCallback = function (error, status){
										//utilService.hideWait();
										utilService.tratarExcecao(error);			  						
									}
							);
						} else {
							//utilService.hideWait();
						}
						angular.element('.calendar').fullCalendar('removeEvents',agendamento.id);				
					},
					errorCallback = function (error, status){	
						//utilService.hideWait();
						utilService.tratarExcecao(error);			  						
					}
				);				
			$uibModalInstance.close();
							
			if ((!psicologoFactory.isVinculadoGCal() && agendamento.grupo > 0) || agendamento.eventoPrincipal) {
				modalAgendamentoFactory.setTipoConfirmacao(consts.TIPOS_CONFIRMACOES.REMOVER_EVENTOS_FUTUROS);
				modalAgendamentoFactory.setMsgConfirmacao("Remover também os eventos futuros?");
				modalAgendamentoService.openConfirmModal();
			}		 				
		};
		
		ctrl.iniciarConsulta = function(agendamento) {		
			consultaFactory.setAgendamento(agendamento);
			if (agendamento.consulta) {						
				$location.path('/consulta');
			} else {
				if (agendamento.paciente) {								
					consultaFactory.setConsulta({});
					consultaFactory.setId(null);				
					consultaFactory.setProntuario(null);
					consultaFactory.setValor(0);
					consultaFactory.setRecibo(false);
					consultaFactory.setInicio(new Date());
					consultaFactory.setFim(null);
					$location.path('/consulta');
				} else {
					$mdDialog.show(
						$mdDialog.alert()
							.clickOutsideToClose(true)
							.title('Algo saiu errado ...')
							.textContent("Não foi possível localizar o paciente da consulta!")
							.ariaLabel('Alerta')
							.ok('Ok')						
					);							
				}
			}
			$uibModalInstance.close();			
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
			agendamento.start = moment(agendamento.start).hour(horarioConsulta[0]).minute(horarioConsulta[60]).second(0).millisecond(0);			
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
		}							
		
		angular.element('.calendar').fullCalendar('unselect');												
	});			
	
	}
	
}]);
