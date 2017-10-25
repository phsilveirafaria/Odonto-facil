var lazyModules = ['ui.calendar', 'ui.bootstrap']; 
angular.forEach(lazyModules, function(dependency) {
	angular.module('odontoFacil').requires.push(dependency);
});

angular.module('odontoFacil').controller('modalAgendamentoController', ['$scope', '$http',
	'$location', '$mdDialog', 'agendamentoFactory', 'modalAgendamentoFactory', 'Session', '$uibModal' , '$uibModalInstance', 'modalAgendamentoService', 'consultaFactory', 'funcionarioFactory',
	function ($scope, $http, $location, $mdDialog,	agendamentoFactory, modalAgendamentoFactory, Session, $uibModal, $uibModalInstance, modalAgendamentoService, consultaFactory, funcionarioFactory) {
	
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
				consultaFactory.setConsulta({});
				consultaFactory.setId(null);				
				consultaFactory.setProntuario(null);
				consultaFactory.setValor(0);
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
	
	
	funcionarioFactory.listarDentistas().then(
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
		
		ctrl.removerAgendamento = function(agendamento){
			
			agendamentoFactory.removerAgendamento(agendamento).then(
					successCallback = function(response) {					
										angular.element('.calendar').fullCalendar('removeEvents',agendamento.id);				
					},errorCallback = function (error, status){
										//utilService.hideWait();
										utilService.tratarExcecao(error);			  						
									}
							);
						angular.element('.calendar').fullCalendar('removeEvents',agendamento.id);				
			$uibModalInstance.close();
							
		
			
		$mdDialog.show(
				$mdDialog.alert()
					.clickOutsideToClose(true)
					.title('Removido com Sucesso')
					.textContent("Agendamento Removido com Sucesso!")
					.ariaLabel('Alerta')
					.ok('Ok')						
			);
		
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
							
			if (!funcionarioFactory.isVinculadoGCal()) {
//				modalAgendamentoFactory.setTipoConfirmacao(consts.TIPOS_CONFIRMACOES.REMOVER_EVENTOS_FUTUROS);
//				modalAgendamentoFactory.setMsgConfirmacao("Remover também os eventos futuros?");
				modalAgendamentoService.openConfirmModal();
			}		 				
		};
		
		 
	 
	ctrl.salvar = function (agendamento) {

		agendamentoFactory.salvarAgendamento(agendamento).then(
					successCallback = function(response) {	  				   					
						var event = angular.element('.calendar').fullCalendar('clientEvents',agendamento.id);																								
						if (event.length > 0) {
							agendamento.title = updateTitle(agendamento);
							$uibModalInstance.close();
							$mdDialog.show(
									$mdDialog.alert()
										.clickOutsideToClose(false)
										.title('Agendamento salvo com sucesso!')
										.textContent("Agendamento salvo com sucesso para " + agendamento.start)
										.ariaLabel('Alerta')
										.ok('Ok')						
								);
							
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
						
//						agendamentoFactory.salvarAgendamentoTemporarioGCalendar(agendamento).then(
//								successCallback = function(response) {
//									//utilService.hideWait();
//									atualizarViewFC();						
//								},
//								errorCallback = function(error) {
//									//utilService.hideWait();
//									utilService.tratarExcecao(error);
//								}
//						);			

		// Novo agendamento
		if (agendamento.cliente) {						
			agendamento.title = updateTitle(agendamento);
			var horarioConsulta = agendamento.formatedStart.split(":");
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
	
	 var atualizarViewFC = function() {	
		 utilService.setMessage("Carregando agendamentos ...");
		 utilService.showWait();		 		 
		 angular.element('.calendar').fullCalendar('removeEvents');
		 // Atualiza a view para o caso de haver algum evento semanal
		 view = angular.element('.calendar').fullCalendar('getView');
		 agendamentoFactory.listarAgendamentos(view.start, view.end).then(
				 successCallback = function (response) {					 					 
					 angular.element('.calendar').fullCalendar('renderEvents',response.data);
					 utilService.hideWait();
				 },
				 errorCallback = function (error) {	
					 utilService.hideWait();
					 utilService.tratarExcecao(error);
				 }
		 );		
	 };
	
}]);
