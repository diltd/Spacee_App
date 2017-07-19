package jp.spacee.app.android.spacee_app;


import jp.spacee.app.android.spacee_app.IICCReaderServiceCallback;


interface  IICCReaderService
{
	void		registerCallback		(in IICCReaderServiceCallback callback);
	void		unregisterCallback		(in IICCReaderServiceCallback callback);

	boolean		ICCInitializeReader		(in BluetoothDevice dev);
	int			ICCOpenReader			();
	int			ICCReadIDm   			();
	int			ICCCloseReader			();
	int			ICCCancelReader			();
}