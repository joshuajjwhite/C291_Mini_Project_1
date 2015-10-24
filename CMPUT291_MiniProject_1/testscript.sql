select flightno, src, dst, dep_date, arr_time, stops, layover, price, seats  
		from ( 
	  		select flightno, src, dst, dep_date, arr_time, stops, layover, price, seats  
		 		from  
			  		(select af.flightno1 (flightno), af.src, af.dst, af.dep_date, af.arr_time, 1 stops, gc.layover, af.price, af.seats 
			  		from good_connections gc, available_flights af 
			  		where gc.flightno1 = af.flightno and to_char(gc.dep_date,DD/MM/YYYY)=  
			  		dep_date  
			  		 and gc.src=  src 
			  		 and gc.dst=  dst   
				union 
					(select af.flightno2 (flightno), af.src, af.dst, af.dep_date, af.arr_time, 1 stops, gc.layover, af.price, af.seats 
			  		from good_connections gc, available_flights af 
			  		where gc.flightno2 = af.flightno and to_char(gc.dep_date,DD/MM/YYYY)=  
			  		dep_date  
			  		 and gc.src=  src 
			  		 and gc.dst=  dst   
				union 
			  		select flightno, src, dst, dep_date, arr_time, 0 stops, 0 layover, price, seats 
			  		from available_flights 
			  		where to_char(dep_date,DD/MM/YYYY)= 
			  		dep_date  
			  		 and src=src 
			  		and dst=dst))) 
SORT BY price;