/*
 * SirDarius <http://stackoverflow.com/questions/3291490/common-class-for-asynctask-in-android>
 */
package me.shkschneider.dropbearserver.task;

public interface DropbearRemoverCallback<T> {

	public void onDropbearRemoverComplete(T result);

}
