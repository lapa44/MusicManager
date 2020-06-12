package org.example.service;

import java.util.ArrayList;
import java.util.List;
import org.example.model.MyFile;
import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

public class VideoConverter {

  public List<MyFile> convertSelectedToMp3s(List<MyFile> sourceFiles) throws EncoderException {
    EncodingAttributes attributes = new EncodingAttributes();
    attributes.setFormat("mp3");
    attributes.setAudioAttributes(setAudioAttributes());
    Encoder encoder = new Encoder();
    List<MyFile> targetFiles = new ArrayList<>();
    for (MyFile file : sourceFiles) {
      String oldFormat = file.getFormat();
      MyFile convertedFile = new MyFile(file.getAbsolutePath().replace(oldFormat, "mp3"));
      encoder.encode(new MultimediaObject(file), convertedFile, attributes);
      targetFiles.add(convertedFile);
    }
    return targetFiles;
  }

  private AudioAttributes setAudioAttributes() {
    AudioAttributes audio = new AudioAttributes();
    audio.setCodec("libmp3lame");
    audio.setBitRate(128000);
    audio.setChannels(2);
    audio.setSamplingRate(44100);
    return audio;
  }
}
