package movistar.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import movistar.bean.Entity;

public class XMLUtil {
	public static void getXMLFromEntity(Entity entity, StringBuilder sb) throws Exception{
		Field[] fields = entity.getClass().getSuperclass().getDeclaredFields();
		sb.append("<").append(entity.getClass().getSimpleName().toLowerCase()).append(">");
		
		for(Field field:fields){
			if(!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())){
			    
				String methodName = String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);
				
				if(boolean.class==field.getType()||Boolean.class==field.getType())
					methodName = "is"+methodName;
				else
					methodName = "get"+methodName;
				Object result = entity.getClass().getMethod(methodName, null).invoke(entity, null);
				
				if(result!=null){
					sb.append("<"+field.getName().toLowerCase()+">").append(escapeXML(result)).append("</"+field.getName().toLowerCase()+">");
				}
			}
		}
		fields = entity.getClass().getDeclaredFields();
		for(Field field:fields){
			if(!Modifier.isStatic(field.getModifiers())){
				String methodName = String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);
				if(boolean.class==field.getType()||Boolean.class==field.getType())
					methodName = "is"+methodName;
				else
					methodName = "get"+methodName;
				Object result = entity.getClass().getMethod(methodName, null).invoke(entity, null);
				if(result!=null){
					if(result instanceof Entity){
						// revisar si colocar la etiqueta como nombre de atributo o como nombre de clase
						getXMLFromEntity((Entity)result, sb);
					}else if(result instanceof List){
						sb.append("<"+field.getName().toLowerCase()+">");
						List<Entity> list = (List<Entity>) result;
						for(Entity ent:list){
							getXMLFromEntity(ent, sb);
						}
						sb.append("</"+field.getName().toLowerCase()+">");
					}else{
						sb.append("<"+field.getName().toLowerCase()+">").append(escapeXML(result)).append("</"+field.getName().toLowerCase()+">");
					}
				}
			}
		}
		sb.append("</").append(entity.getClass().getSimpleName().toLowerCase()).append(">");
	}
	static String escapeXML(Object value){
	    return String.valueOf(value).replaceAll("\\&", "&amp;").replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
	}
}