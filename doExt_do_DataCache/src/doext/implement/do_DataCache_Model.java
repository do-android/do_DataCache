package doext.implement;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import core.DoServiceContainer;
import core.helper.DoJsonHelper;
import core.interfaces.DoIScriptEngine;
import core.object.DoInvokeResult;
import core.object.DoSingletonModule;
import doext.define.do_DataCache_IMethod;

/**
 * 自定义扩展SM组件Model实现，继承DoSingletonModule抽象类，并实现do_DataCache_IMethod接口方法；
 * #如何调用组件自定义事件？可以通过如下方法触发事件：
 * this.model.getEventCenter().fireEvent(_messageName, jsonResult);
 * 参数解释：@_messageName字符串事件名称，@jsonResult传递事件参数对象； 获取DoInvokeResult对象方式new
 * DoInvokeResult(this.getUniqueKey());
 */
public class do_DataCache_Model extends DoSingletonModule implements do_DataCache_IMethod {

	private SharedPreferences sp;

	public do_DataCache_Model() throws Exception {
		super();
		Activity _activity = DoServiceContainer.getPageViewFactory().getAppContext();
		sp = _activity.getSharedPreferences("deviceone", Context.MODE_PRIVATE);
	}

	/**
	 * 同步方法，JS脚本调用该组件对象方法时会被调用，可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public boolean invokeSyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		if ("loadData".equals(_methodName)) {
			this.loadData(_dictParas, _scriptEngine, _invokeResult);
			return true;
		} else if ("saveData".equals(_methodName)) {
			this.saveData(_dictParas, _scriptEngine, _invokeResult);
			return true;
		} else if ("hasData".equals(_methodName)) {
			this.hasData(_dictParas, _scriptEngine, _invokeResult);
			return true;
		} else if ("removeData".equals(_methodName)) {
			this.removeData(_dictParas, _scriptEngine, _invokeResult);
			return true;
		} else if ("removeAll".equals(_methodName)) {
			this.removeAll(_dictParas, _scriptEngine, _invokeResult);
			return true;
		}
		return super.invokeSyncMethod(_methodName, _dictParas, _scriptEngine, _invokeResult);
	}

	private void removeAll(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) {
		Editor _editor = sp.edit();
		_editor.clear();
		boolean _result = _editor.commit();
		_invokeResult.setResultBoolean(_result);
	}

	private void removeData(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws JSONException {
		String _key = DoJsonHelper.getString(_dictParas, "key", "");
		boolean _result = false;
		if (sp.contains(_key)) {
			Editor _editor = sp.edit();
			_editor.remove(_key);
			_result = _editor.commit();
		}
		_invokeResult.setResultBoolean(_result);
	}

	private void hasData(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws JSONException {
		String _key = DoJsonHelper.getString(_dictParas, "key", "");
		_invokeResult.setResultBoolean(sp.contains(_key));
	}

	/**
	 * 异步方法（通常都处理些耗时操作，避免UI线程阻塞），JS脚本调用该组件对象方法时会被调用， 可以根据_methodName调用相应的接口实现方法；
	 * 
	 * @_methodName 方法名称
	 * @_dictParas 参数（K,V）
	 * @_scriptEngine 当前page JS上下文环境
	 * @_callbackFuncName 回调函数名 #如何执行异步方法回调？可以通过如下方法：
	 *                    _scriptEngine.callback(_callbackFuncName,
	 *                    _invokeResult);
	 *                    参数解释：@_callbackFuncName回调函数名，@_invokeResult传递回调函数参数对象；
	 *                    获取DoInvokeResult对象方式new
	 *                    DoInvokeResult(this.getUniqueKey());
	 */
	@Override
	public boolean invokeAsyncMethod(String _methodName, JSONObject _dictParas, DoIScriptEngine _scriptEngine, String _callbackFuncName) throws Exception {
		return super.invokeAsyncMethod(_methodName, _dictParas, _scriptEngine, _callbackFuncName);
	}

	/**
	 * 加载数据；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void saveData(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String _key = DoJsonHelper.getString(_dictParas, "key", "");
		String _value = DoJsonHelper.getString(_dictParas, "value", "");
		Editor _editor = sp.edit();
		_editor.putString(_key, _value);
		boolean _result = _editor.commit();
		_invokeResult.setResultBoolean(_result);
	}

	/**
	 * 缓存数据；
	 * 
	 * @_dictParas 参数（K,V），可以通过此对象提供相关方法来获取参数值（Key：为参数名称）；
	 * @_scriptEngine 当前Page JS上下文环境对象
	 * @_invokeResult 用于返回方法结果对象
	 */
	@Override
	public void loadData(JSONObject _dictParas, DoIScriptEngine _scriptEngine, DoInvokeResult _invokeResult) throws Exception {
		String _key = DoJsonHelper.getString(_dictParas, "key", "");
		String _value = sp.getString(_key, "");
		_invokeResult.setResultText(_value);
	}
}