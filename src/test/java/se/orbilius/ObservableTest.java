package se.orbilius;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ObservableTest {

	@Test
	public void shouldReSubscribeOnObservable() throws Exception{
		Observable<String> obs = Observable.just("1", "2", "3", "4", "5");
		
		Thread.sleep(3000);

		obs.subscribe(new Subscriber<String>(){

			@Override
			public void onCompleted() {
				System.out.println("Completed");
				obs.subscribe(new Subscriber<String>(){

					@Override
					public void onCompleted() {
						System.out.println("Completed again");
					}

					@Override
					public void onError(Throwable e) {
						System.out.println("got error again");
					}

					@Override
					public void onNext(String t) {
						System.out.println("got " + t + " again");
					}
					
				});				
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("got error");
			}

			@Override
			public void onNext(String t) {
				System.out.println("got " + t);
			}
		});
	}
	
	@Test
	public void shouldUseSchedulerOnSameThreadToSubscribe() throws Exception{
		Observable<String> obs = Observable.just("1", "2", "3", "4", "5");
		
		System.out.println("Starting on " + geturrentThreadName());
		Thread.sleep(3000);

		Observable<String> subscribeOn = obs.subscribeOn(Schedulers.immediate());
		ThreadPrintingSubscriber subscriber = new ThreadPrintingSubscriber();
		subscribeOn.subscribe(subscriber);
		
		assertTrue(subscriber.message.equals("Completed"));
	}
	
	@Test
	public void shouldUseSchedulerOnIOThreadToSubscribe() throws Exception{
		Observable<String> obs = Observable.just("1", "2", "3", "4", "5");

		System.out.println("Starting on " + geturrentThreadName());
		Thread.sleep(3000);

		Observable<String> subscribeOn = obs.subscribeOn(Schedulers.io());
		ThreadPrintingSubscriber subscriber = new ThreadPrintingSubscriber();
		subscribeOn.subscribe(subscriber);			
		assertTrue(subscriber.message == null);
	}
	
	class ThreadPrintingSubscriber extends Subscriber<String>{

			public String message;
			@Override
			public void onCompleted() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Completed");
				message = "Completed";
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("got error");
				
			}

			@Override
			public void onNext(String t) {
				System.out.println("got " + t + " on " + geturrentThreadName());
			}

	}

	private String geturrentThreadName() {
		return Thread.currentThread().getName();
	}
	
}
