/*
 * SirDarius <http://stackoverflow.com/questions/3291490/common-class-for-asynctask-in-android>
 */
package me.shkschneider.dropbearserver.task;

public interface ServerStarterCallback<T> {

	public void onServerStarterComplete(T result);

}
