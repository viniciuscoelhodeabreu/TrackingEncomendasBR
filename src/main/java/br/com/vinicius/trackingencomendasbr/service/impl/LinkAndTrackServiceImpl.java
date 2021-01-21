package br.com.vinicius.trackingencomendasbr.service.impl;

import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.vinicius.trackingencomendasbr.dto.TrackingDTO;
import br.com.vinicius.trackingencomendasbr.service.ILinkAndTrackService;
import br.com.vinicius.trackingencomendasbr.util.Constants;
import br.com.vinicius.trackingencomendasbr.util.HttpUtil;
import lombok.SneakyThrows;

@Service
public class LinkAndTrackServiceImpl implements ILinkAndTrackService {

	@Override
	@SneakyThrows
	public TrackingDTO getTrackingByTrackCode(String trackCode) {
		Optional<HttpResponse> httpResponse = HttpUtil.doGet(Constants.getLinkAndTrackUrl(System.getenv("LINKANDTRACK_USER"), System.getenv("LINKANDTRACK_TOKEN"), trackCode));
		String response = EntityUtils.toString(httpResponse.get().getEntity());
		
		TrackingDTO tracking = new Gson().fromJson(response, TrackingDTO.class);
		
		return tracking;
	}

}
