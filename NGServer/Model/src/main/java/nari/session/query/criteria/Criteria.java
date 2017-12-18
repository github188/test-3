package nari.session.query.criteria;

import nari.session.query.Query;

public class Criteria {

//	Criteria.and(Criteria.eq("name","111111"),Criteria.eq("age","22"),Criteria.eq("num","123"));
//	Criteria.or(Criteria.eq("name","111111"),Criteria.eq("age","22"))
//	Criteria.and(Criteria.eq("name","111111"),Criteria.or(Criteria.eq("name","111111"),Criteria.eq("age","22")));
//	
//	Concater   Operater
//	Criteria.and().append(Criteria.eq("name","111111"),Criteria.in("age",new Object[]{11,22,33}),Criteria.gt("num","100"));
//	Criteria.or().append(Criteria.eq("name","111111"),Criteria.in("age",new Object[]{11,22,33}),Criteria.gt("num","100"));
//	
	
	
	
	public static Concater and(){
		return new AndConcater();
	}
	
	public static Concater or(){
		return new OrConcater();
	}
	
	public static Operater eq(String key,Object val){
		return new EqOperater(key,val);
	}
	
	public static Operater neq(String key,Object val){
		return new NeqOperater(key,val);
	}
	
	public static Operater gt(String key,Object val){
		return new GtOperater(key,val);
	}
	
	public static Operater gte(String key,Object val){
		return new GteOperater(key,val);
	}
	
	public static Operater lt(String key,Object val){
		return new LtOperater(key,val);
	}
	
	public static Operater lte(String key,Object val){
		return new LteOperater(key,val);
	}
	
	public static Operater in(String key,Object[] val){
		return new InOperater(key,val);
	}
	
	public static Operater in(String key,Query query){
		return new InQueryOperater(key,query);
	}
	
	public static Operater match(String key,String pattern){
		return new MatcherOperater(key,pattern);
	}
	
	public static Operater isEmpty(String key){
		return new EmptyOperater(key);
	}
	
	public static Operater notEmpty(String key){
		return new NotEmptyOperater(key);
	}
	
	public static Operater polygon(String key,double[] coordinates){
		return new PolygonOperater();
	}
	
	public static Operater circle(String key,double[] coordinates){
		return new CircleOperater();
	}

	public static Operater polyline(String key,double[] coordinates){
		return new PolylineOperater();
	}
	
	public static Operater point(String key,double[] coordinates){
		return new PointOperater();
	}
}
