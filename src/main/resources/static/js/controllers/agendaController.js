// Modulos desta controller
//var lazyModules = ['ui.calendar', 'ui.bootstrap'];
 var lazyModules = ['ui.calendar', 'ui.bootstrap']; 
angular.forEach(lazyModules, function(dependency) {
	angular.module('odontoFacil').requires.push(dependency);
});

angular.module('odontoFacil').controller('agendaController', ['$scope', '$mdDialog', 'agendamentoFactory', '$uibModal',
	function ($scope, $mdDialog, agendamentoFactory, $uibModal) {
	
  var ctrl = this;
  
  
  $scope.$watch(function () { return ctrl.funcionario; }, function (newValue, oldValue) {
	});
  
 
    
  var select = function(start, end) {	  
	  console.log("select");
	  limparDadosAgendamento();
	  
	  agendamentoFactory.setAgendamentoCarregado(null);	  

	  // Verifica se existe um horario pre definido
	  if (!start.hasTime()) {		
		  var time = moment();
		  start = moment(start).hour(time.hour()).minute(time.minute()).second(0).millisecond(0);
		  end = moment(start); // a consulta deve terminar no mesmo dia
		  end.add(1, 'm');
	  }
		
	  var dataInicialAgendamento = start.local();
	  var dataFinalAgendamento = end.local();
		
	  agendamentoFactory.setStart(new Date(dataInicialAgendamento));
	  agendamentoFactory.setEnd(new Date(dataFinalAgendamento));
	  agendamentoFactory.setFormatedStart(start.format('HH:mm'));		
	  agendamentoFactory.setEditable(true);
	  
	  ctrl.openEventModal();
  };
  
  
//  /**
//	 * Abre janela modal de erro
//	 */	
//	this.openConfirmModal = function (size) {	 	
//		var modalInstance = $uibModal.open({
//			animation: true,
//		    ariaLabelledBy: 'modal-title',
//		    ariaDescribedBy: 'modal-body',
//		    templateUrl: 'pages/confirmacao_agendamento_modal.html',
//		    controller: 'ModalAgendamentoCtrl',
//		    controllerAs: 'ctrl',      
//		    size: size
//		});    
//	};
	
	/**
	 * Abre janela modal do agendamento
	 */	
	this.openEventModal = function (size) {	 	
		var modalInstance = $uibModal.open({
			animation: true,
		    ariaLabelledBy: 'modal-title',
		    ariaDescribedBy: 'modal-body',
		    templateUrl: 'pages/modal_agendamento.html',
		    controller: 'modalAgendamentoController',
		    controllerAs: 'ctrl',
		    size: size
		});
	    
		modalInstance.result.then(function (selectedItem) {}, function () {			
			agendamentoFactory.setAgendamento({});					
		});
	};
//	  limparDadosAgendamento();	  
//	  agendamentoFactory.setAgendamentoCarregado(null);	  
//
//	  // Verifica se existe um horario pre definido
//	  if (!start.hasTime()) {		
//		  var time = moment();
//		  start = moment(start).hour(time.hour()).minute(time.minute()).second(0).millisecond(0);
//		  end = moment(start); // a consulta deve terminar no mesmo dia
//		  end.add(1, 'm');
//	  }
//		
//	  var dataInicialAgendamento = start.local();
//	  var dataFinalAgendamento = end.local();
//		
//	  agendamentoFactory.setStart(new Date(dataInicialAgendamento));
//	  agendamentoFactory.setEnd(new Date(dataFinalAgendamento));
//	  agendamentoFactory.setFormatedStart(start.format('HH:mm'));		
//	  agendamentoFactory.setEditable(true);
//	  
//	  modalAgendamentoService.openEventModal();
//  };
	
  var eventClick = function(event, jsEvent, view) {	  
	  console.log("click");
	  event.formatedStart = event.start.format('HH:mm');							
	  agendamentoFactory.setAgendamento(angular.copy(event));
	  agendamentoFactory.setAgendamentoCarregado(angular.copy(event));	  	  
	  
	  ctrl.openEventModal();											
  };
	
  var eventRender = function( event, element, view ) { 	  
	  console.log("eventRender");
//	  if (psicologoFactory.isVinculadoGCal() && ((event.paciente == null && event.idGCalendar) ||
//		 (event.eventoPrincipal))) {		  
//		  event.editable = false;
//	  } else {
//		  event.editable = true;
//	  }	  
  }
  
  var limparDadosAgendamento = function() {
	  agendamentoFactory.setAgendamento({});
  };
  
  var eventDrop = function(event, delta, revertFunc, jsEvent, ui, view) {	
	  console.log("EvendDrop");
//  	  var oldEvent = angular.copy(event); // evento dropado			  
//	    	  
//	  var days = moment.duration(delta).days()*(-1);
//	  oldEvent.start.add(days, "d");
//	  oldEvent.end.add(days, "d");						
//		
//	  var horas   = event.end.hours();
//	  var minutos = event.end.minutes();
//			
//	  event.end  = moment(event.start);
//	  event.end  = moment(event.end).hours(horas).minutes(minutos);								
//		
//	  updateEventDroped(angular.copy(event), angular.copy(oldEvent));  
  };
	
  var viewRender = function (view, element) {	  
	  console.log("viewRender");
//	  listarAgendamento(view.start, view.end);	 
  };    

  /* config object */
  ctrl.uiConfig = {
		  calendar: ({
			  header : {
				  left : 'prev,next today',
				  center : 'title',
				  right : 'month,agendaWeek,agendaDay'
			  },		
			  //height: 450,
			  timezone: "local",
			  allDaySlot: false,
			  locale : 'pt-br',
			  navLinks : true, // can click day/week names to navigate views
			  selectable : true,
			  slotDuration : "00:30:00",
			  slotLabelFormat : [ 'ddd D/M', 'HH:mm' ],
			  timeFormat : "HH:mm",
			  selectHelper : true,		
			  editable : true,
			  eventLimit : true, // allow "more" link when too many events
			  select : select,
			  eventClick : eventClick,		
			  eventDrop : eventDrop,
			  eventRender: eventRender,
			  viewRender: viewRender			
		  })
  };        
  
  /**
   * Limpa os dados pertinentes a um agendamento
   */
  var limparDadosAgendamento = function() {
	//  agendamentoFactory.setAgendamento({});
  };
  
  /**
   * Popula o calendario com os agendamentos do BD e persiste novos agendamentos
   * na view atual, caso necessário
   */ 
 
}]);