package gec.scf.dummy.common.serialize;

import com.fasterxml.jackson.databind.util.StdConverter;

import io.seruco.encoding.base62.Base62;

public class GecEncodeLongSerialize extends StdConverter<Long, String> {
	@Override
	public String convert(Long value) {
		String result = null;
		if (value != null){
			Base62 base62 = Base62.createInstance();
			byte[] encoded = base62.encode(String.valueOf(value).getBytes());
			result =  new String(encoded);
		}
		return result;
	}
}
