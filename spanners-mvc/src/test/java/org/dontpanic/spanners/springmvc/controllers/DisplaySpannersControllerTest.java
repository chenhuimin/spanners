package org.dontpanic.spanners.springmvc.controllers;

import static org.dontpanic.spanners.springmvc.controllers.DisplaySpannersController.*;
import static org.dontpanic.spanners.stubs.SpannersStubs.*;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.dontpanic.spanners.dao.Spanner;
import org.dontpanic.spanners.dao.SpannersDAO;
import org.dontpanic.spanners.springmvc.exception.SpannerNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Test for page that displays list of spanners
 * User: Stevie
 * Date: 05/10/13
 */
@RunWith(MockitoJUnitRunner.class)
public class DisplaySpannersControllerTest {

    @Mock private SpannersDAO spannersDAO;
    @InjectMocks private DisplaySpannersController controller = new DisplaySpannersController();

    /**
     * Test that page shows list of spanners
     */
    @Test
    public void testDisplaySpanners() {

        // Stub behaviours - list of spanners
        when(spannersDAO.getAll()).thenReturn(SPANNERS);

        // Call the controller
        ModelAndView response = controller.displaySpanners();
        assertNotNull("no response", response);

        // Assert that we forward to correct page
        assertEquals("view name", VIEW_DISPLAY_SPANNERS, response.getViewName());

        // Assert that spanners are in model
        @SuppressWarnings("unchecked")
        List<Spanner> spannersInModel = (List<Spanner>)response.getModelMap().get(MODEL_ATTRIBUTE_SPANNERS);
        assertNotNull(MODEL_ATTRIBUTE_SPANNERS + " is null", spannersInModel);
        assertEquals(SPANNERS, spannersInModel);

    }


    /**
     * Test for delete spaner action on display spanners page
     */
    @Test
    public void testDeleteSpanner() throws Exception {

        // Stub behaviours - spanner exists to be deleted
        when(spannersDAO.get(SPANNER_ID)).thenReturn(SPANNER);

        // Request a spanner is deleted
        ModelAndView response = controller.deleteSpanner(SPANNER_ID);
        assertNotNull(response);

        // Verify that spanner was deleted
        verify(spannersDAO).delete(SPANNER);

        // Assert spanners list page is shown after deletion
        assertEquals("view name", VIEW_DISPLAY_SPANNERS, response.getViewName());
    }


    /**
     * Test attempt to delete a spanner that does not exist
     */
    @Test
    public void testDeleteSpannerNotFound() throws Exception  {

        // Stub behaviours - spanner to be deleted does not exist
        when(spannersDAO.get(SPANNER_ID)).thenReturn(null);

        // Request a spanner is deleted
        try {
               controller.deleteSpanner(SPANNER_ID);
               fail("Expected: SpannerNotFoundException");
           } catch(SpannerNotFoundException notFoundEx) {
               assertEquals("Exception spanner id", SPANNER_ID, notFoundEx.getSpannerId());
           }

    }

}
