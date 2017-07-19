package jp.spacee.app.android.spacee_app;


interface  IICCReaderServiceCallback
{
	void		ICCR_InitComp			(in int retcode);
	void		ICCR_OpenComp			(in int retcode);
	void		ICCR_ReadData			(in String result);
	void		ICCR_Attention			(in int attentionKind, in String info);
}