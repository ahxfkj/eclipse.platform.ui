package org.eclipse.ui.tests.api;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;
import org.eclipse.jdt.junit.util.*;

public class IWorkbenchWindowTest extends UITestCase {

	private IWorkbenchWindow fWin;

	public IWorkbenchWindowTest(String testName) {
		super(testName);
	}

	public void setUp() {
		fWin = openTestWindow();
	}

	public void testClose() throws Throwable {		
		assertEquals(fWin.close(), true);
		assertEquals(ArrayUtil.contains(fWorkbench.getWorkbenchWindows(), fWin), false);
	}

	public void testGetActivePage() throws Throwable {
		IWorkbenchPage page1, page2;
		page1 = openTestPage(fWin);
		assertEquals(fWin.getActivePage(), page1);

		page2 = openTestPage(fWin);
		assertEquals(fWin.getActivePage(), page2);

		fWin.setActivePage(page1);
		assertEquals(fWin.getActivePage(), page1);

		fWin.setActivePage(page2);
		assertEquals(fWin.getActivePage(), page2);

		//no pages
		closeAllPages(fWin);
		assertNull(fWin.getActivePage());
	}

	public void testSetActivePage() throws Throwable {
		openTestPage(fWin, 5);
		IWorkbenchPage[] pages = fWin.getPages();

		for (int i = 0; i < pages.length; i++) {
			fWin.setActivePage(pages[i]);
			assertEquals(pages[i], fWin.getActivePage());
		}
		
		fWin.setActivePage( null );
		assertNull( fWin.getActivePage() );
	}

	public void testGetPages() throws Throwable {
		int totalBefore;
		IWorkbenchPage[] pages, domainPages;

		totalBefore = fWin.getPages().length;
		int num = 5;
		pages = openTestPage(fWin, num);
		assertEquals(fWin.getPages().length, totalBefore + num);

		domainPages = fWin.getPages();
		for (int i = 0; i < pages.length; i++)
			assertEquals(ArrayUtil.contains(domainPages, pages[i]), true);

		closeAllPages(fWin);
		assertEquals(fWin.getPages().length, 0);
	}

	public void testGetShell() {
		Shell sh = fWin.getShell();
		assertNotNull(sh);
	}
	
	public void testGetWorkbench() {
		IWorkbenchWindow win = fWorkbench.getActiveWorkbenchWindow();
		assertEquals(win.getWorkbench(), fWorkbench);
	}

	/**
	 * tests openPage(String)
	 */
	public void testOpenPage() throws Throwable {
		IWorkbenchPage page = null;
		try {
			page = fWin.openPage(ResourcesPlugin.getWorkspace());
			assertNotNull(page);
			assertEquals(fWin.getActivePage(), page);
		} finally {
			if (page != null)
				page.close();
		}
	}

	/**
	 * tests openPage(String, IAdaptable)
	 */
	public void testOpenPage2() throws Throwable {
		IWorkbenchPage page = null;
		try {
			page = fWin.openPage(EmptyPerspective.PERSP_ID, ResourcesPlugin.getWorkspace());
			assertNotNull(page);
			assertEquals(fWin.getActivePage(), page);
			assertEquals(
				fWin.getActivePage().getPerspective().getId(),
				EmptyPerspective.PERSP_ID);
		} finally {
			if (page != null)
				page.close();
		}

		//test openPage() fails
		try {
			page = fWin.openPage("*************", ResourcesPlugin.getWorkspace());
			fail();
		} catch (WorkbenchException ex) {
		}

		page.close();
	}

	public void testIsApplicationMenu() {
		String[] ids = {
			IWorkbenchActionConstants.M_FILE,
			IWorkbenchActionConstants.M_VIEW,
			IWorkbenchActionConstants.M_VIEW,
			IWorkbenchActionConstants.M_WORKBENCH,
		};

		for( int i = 0; i < ids.length; i ++ )
			assertEquals( fWin.isApplicationMenu( ids[ i ] ), true );
		
		ids = new String[] {
			IWorkbenchActionConstants.M_EDIT,
			IWorkbenchActionConstants.M_HELP,
			IWorkbenchActionConstants.M_LAUNCH
		};

		for( int i = 0; i < ids.length; i ++ )
			assertEquals( fWin.isApplicationMenu( ids[ i ] ), false );
	}
}