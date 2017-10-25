package br.com.odontofacil.ws.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.odontofacil.model.Agendamento;
import br.com.odontofacil.model.TmpGCalendarEvent;
import br.com.odontofacil.ws.repository.AgendamentoRepository;
import br.com.odontofacil.ws.repository.TmpGCalendarEventRepository;

@Service
public class TmpGCalendarEventService {
	
	@Autowired
	private TmpGCalendarEventRepository tmpGCalendarEventRepository;
	
	public List<String> listarIdGCalendarPorPeriodo(Calendar di, Calendar df){
		List<String>lstGCalendarIds_TmpGCalendarTable = tmpGCalendarEventRepository.listarIdGCalendarPorPeriodo(di, df);
		return lstGCalendarIds_TmpGCalendarTable;
	}
	
	public TmpGCalendarEvent findByIdGCalendar(String idGCalendar){
		return tmpGCalendarEventRepository.findByIdGCalendar(idGCalendar);
	}
	
	public TmpGCalendarEvent save(TmpGCalendarEvent t){
		return tmpGCalendarEventRepository.save(t);
	}
	
	public Iterable<TmpGCalendarEvent> save(Iterable<TmpGCalendarEvent> list){
		return tmpGCalendarEventRepository.save(list);
	}
	
	public void deleteByIdRecurring(String idRecurring)	{
		tmpGCalendarEventRepository.deleteByIdGCalendar(idRecurring);
	}
	
	public void deleteByIdGCalendar(String idGCalendar){
		tmpGCalendarEventRepository.deleteByIdGCalendar(idGCalendar);
	}
	

}
