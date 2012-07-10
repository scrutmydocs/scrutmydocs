/*
 * Licensed to scrutmydocs.org (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.scrutmydocs.webapp.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Display Objects as Strings
 * @author DPI
 */
public class StringTools {
	public StringTools() {
	}

	private static String findEndOfClassName(Class<?> c) {
		String nomClasse = c.getName();
		return nomClasse.substring(nomClasse.lastIndexOf(".") + 1);
	}

	private static String fillComma(int numObjet) {
		if (numObjet > 0) return ",";
		return "";
	}

	private static String fillIndentation(int indentation) {
		StringBuffer sbf = new StringBuffer();
		int i = indentation;
		while (i-- > 0) {
			sbf.append("\t");
		}
		return sbf.toString();
	}

	private static void stringToString(StringBuffer result, String nom, Object valeur, int numObjet) {
		result.append(fillComma(numObjet) + nom + "=\"" + valeur + "\"");
	}

	private static void booleanToString(StringBuffer result, String nom, Object valeur, int numObjet) {
		result.append(fillComma(numObjet) + nom + "=" + valeur);
	}

	private static void integerToString(StringBuffer result, String nom, Object valeur, int numObjet) {
		result.append(fillComma(numObjet) + nom + "=" + valeur);
	}

	private static void doubleToString(StringBuffer result, String nom, Object valeur, int numObjet) {
		result.append(fillComma(numObjet) + nom + "=" + valeur);
	}

	private static void longToString(StringBuffer result, String nom, Object valeur, int numObjet) {
		result.append(fillComma(numObjet) + nom + "=" + valeur);
	}

	private static void dateToString(StringBuffer result, String nom, Object valeur, int numObjet) {
		SimpleDateFormat sdfValeur = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
		result.append(fillComma(numObjet) + nom + "=" + sdfValeur.format(valeur));
	}

	private static void collectionToString(StringBuffer result, String nom, Collection<?> valeur, int numObjet, int indentation) {
		if (valeur.size() != 0) {
			Iterator<?> itValeur = valeur.iterator();
			result.append(fillComma(numObjet) + "\n");
			result.append(fillIndentation(indentation));
			result.append(nom + "=[");
			int nb = 1;
			indentation++;
			while (itValeur.hasNext()) {
				// Recursive
				result.append("\n");
				result.append(fillIndentation(indentation));
				result.append(nb);
				result.append(" - ");
				toString(result, itValeur.next(), indentation);
				nb++;
			}
			indentation--;
			result.append("\n");
			result.append(fillIndentation(indentation) + "]");
		}
	}

	/**
	 * Generate a String representation of an object
	 * 
	 * @param obj Object
	 * @return The generated String
	 */
	public static String toString(Object obj) {
		StringBuffer out = new StringBuffer();
		try {
			toString(out, obj, 0);
		}
		catch (Exception e) {
			return null;
		}

		return out.toString();
	}

	private static void toStringEntete(StringBuffer result, Object obj) {
		result.append(findEndOfClassName(obj.getClass()) + "=[");
	}

	private static void toStringPiedDePage(StringBuffer result) {
		result.append("]");
	}

	private static int toStringContent(StringBuffer result, String nom, Object valeur, int numObjet, int indentation) {
		if (valeur != null) {
			if (valeur instanceof java.lang.String) {
				stringToString(result, nom, valeur, numObjet++);
			}
			if (valeur instanceof java.lang.Boolean) {
				booleanToString(result, nom, valeur, numObjet++);
			}
			if (valeur instanceof java.lang.Integer) {
				integerToString(result, nom, valeur, numObjet++);
			}
			if (valeur instanceof java.lang.Double) {
				doubleToString(result, nom, valeur, numObjet++);
			}
			if (valeur instanceof java.lang.Long) {
				longToString(result, nom, valeur, numObjet++);
			}
			if (valeur instanceof java.util.Date) {
				dateToString(result, nom, valeur, numObjet++);
			}

			if (valeur instanceof java.util.Collection<?>) {
				Collection<?> col = (Collection<?>) valeur;
				if (col.size() != 0) {
					collectionToString(result, nom, col, numObjet++, indentation);
				}
			}
			if (valeur instanceof java.util.Map<?,?>) {
				Map<?,?> col = (Map<?,?>) valeur;
				if (col.size() != 0) {
					collectionToString(result, nom, col.values(), numObjet++, indentation);
				}
			}
		}
		return numObjet;
	}

	private static Map<String, Object> getGetters(Object bean) {
		Map<String, Object> description = new HashMap<String, Object>();
		if (bean == null) {
			throw new IllegalArgumentException("No bean specified");
		}

		Class<?> beanClass = bean.getClass();

		PropertyDescriptor[] descriptors = null;
		BeanInfo beanInfo = null;

		try {
			beanInfo = Introspector.getBeanInfo(beanClass);
			descriptors = beanInfo.getPropertyDescriptors();
			if (descriptors == null) return null;

			for (int i = 0; i < descriptors.length; i++) {
				PropertyDescriptor descriptor = (PropertyDescriptor) descriptors[i];
				String propName = descriptor.getName().substring(0, 1).toUpperCase() + descriptor.getName().substring(1);
				String methodName = descriptor.getReadMethod() != null ? descriptor.getReadMethod().getName() : "get" + propName;
				Method readMethod = null;
				try {
					readMethod = beanClass.getMethod(methodName, new Class[0]);
					if (readMethod != null) descriptor.setReadMethod(readMethod);
					}
					catch (Exception e) {
					}
				}
			}
		catch (IntrospectionException e) {
			return null;
		}

		for (int i = 0; i < descriptors.length; i++) {
			String name = descriptors[i].getName();
			if (descriptors[i].getReadMethod() != null) {
				try {
					description.put(name, invoquerMethode(bean, name, new Class[0], new Object[0]));
				}
				catch (Exception e) {
				}
			}
		}

		return (description);
	}

	private static Object invoquerMethode(Object targetObject, String nomMethode, Class<?>[] parametersClass, Object[] parametres)
			throws IllegalAccessException, InvocationTargetException {
		String propName = nomMethode.substring(0, 1).toUpperCase() + nomMethode.substring(1);
		String methodName = "get" + propName;
		
		Method method = null;
		try {
			method = targetObject.getClass().getMethod(methodName, parametersClass);
		}
		catch (Exception e) {
			methodName = "is" + propName;
			try {
				method = targetObject.getClass().getMethod(methodName, parametersClass);
			}
			catch (Exception e2) {
				return null;
			}
		}
		
		if (method == null) return null;
		Object obj = method.invoke(targetObject, parametres);
		return obj;
	}

	private static Map<String, Object> describe(Object obj) {
		Map<String, Object> map = null;
		try {
			map = getGetters(obj);
		}
		catch (Exception e) {
		}
		return map;
	}

	private static void toString(StringBuffer result, Object obj, int indentation) {

		toStringEntete(result, obj);

		Iterator<Entry<String,Object>> it = describe(obj).entrySet().iterator();

		int numObjet = 0;
		while (it.hasNext()) {
			Entry<String,Object> entry = it.next();
			String nom = entry.getKey();
			Object valeur = entry.getValue();

			if (valeur != null) numObjet = toStringContent(result, nom, valeur, numObjet, indentation);
		}
		toStringPiedDePage(result);
	}
}
